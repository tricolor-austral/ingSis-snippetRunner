package ingsis.tricolor.snippetrunner.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import ingsis.tricolor.snippetrunner.model.dto.FormatFile
import ingsis.tricolor.snippetrunner.model.dto.LintFile
import ingsis.tricolor.snippetrunner.model.service.FormatterRulesService
import ingsis.tricolor.snippetrunner.model.service.LinterRulesService
import ingsis.tricolor.snippetrunner.service.interfaces.Service
import org.example.Output
import org.example.executer.Executer
import org.example.executer.FormatterExecuter
import org.example.executer.LinterExecuter
import org.example.staticCodeeAnalyzer.SCAOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.UUID

@org.springframework.stereotype.Service
class PrintScriptService
    @Autowired
    constructor(
        private val formatterService: FormatterRulesService,
        private val linterRulesService: LinterRulesService,
        @Value("\${permission.url}") private val permissionUrl: String,
    ) : Service {
        companion object {
            fun objectMapper(): ObjectMapper {
                val mapper = ObjectMapper()
                mapper.propertyNamingStrategy = PropertyNamingStrategies.UPPER_CAMEL_CASE
                return mapper
            }
        }

        val operationsApi = WebClient.builder().baseUrl("http://$permissionUrl/v1/asset").build()

        override fun runScript(
            input: InputStream,
            version: String,
        ): Output {
            val executer = Executer()
            return executer.execute(input, version)
        }

        override fun test(
            input: String,
            output: List<String>,
            snippet: String,
        ): String {
            val executer = Executer()
            val inputStream = ByteArrayInputStream(snippet.toByteArray())
            val value = executer.execute(inputStream, "1.1", input)
            val result = value.string.split("\n")
            println("result: $result")
            println("expected output: $output")
            for (i in 0 until output.size) {
                if (result[i] != output[i]) {
                    return "failure"
                }
            }
            return "success"
        }

        override fun runLinter(
            input: InputStream,
            version: String,
            userId: String,
            correlationId: UUID,
        ): MutableList<SCAOutput> {
            val defaultPath = "./$userId-linterRules.json"
            val lintRules = linterRulesService.getLinterRulesByUserId(userId, correlationId)
            val linterDto =
                LintFile(
                    lintRules.identifier,
                    lintRules.printwithoutexpresion,
                    lintRules.readinputwithoutexpresion,
                )
            val rulesFile = File(defaultPath)
            objectMapper().writeValue(rulesFile, linterDto)
            val linter = LinterExecuter()
            if (rulesFile.exists()) {
                rulesFile.delete()
            }
            return linter.execute(input, version, defaultPath)
        }

        override fun format(
            snippetId: String,
            input: InputStream,
            version: String,
            userId: String,
            correlationId: UUID,
        ): Output {
            val defaultPath = "./$userId-formatterRules.json"
            val formatterRules = formatterService.getFormatterRulesByUserId(userId, correlationId)
            val formatterDto =
                FormatFile(
                    formatterRules.NewLinesBeforePrintln,
                    formatterRules.SpacesBeforeDeclaration,
                    formatterRules.SpacesAfterDeclaration,
                    formatterRules.SpacesInAssignation,
                )
            val rulesFile = File(defaultPath)
            objectMapper().writeValue(rulesFile, formatterDto)
            val formatter = FormatterExecuter()
            val output = formatter.execute(input, version, defaultPath)
            if (rulesFile.exists()) {
                rulesFile.delete()
            }
            updateOnBucket(snippetId, output.string)
            return output
        }

        fun updateOnBucket(
            key: String,
            content: String,
        ) {
            val responseStatus =
                operationsApi
                    .post()
                    .uri("/snippets/{key}", key)
                    .bodyValue(content)
                    .exchangeToMono { clientResponse ->
                        if (clientResponse.statusCode() == HttpStatus.CREATED) {
                            Mono.just(HttpStatus.CREATED)
                        } else {
                            Mono.just(HttpStatus.BAD_REQUEST)
                        }
                    }.block()
            println(responseStatus)
        }
    }
