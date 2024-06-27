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
import java.io.File
import java.io.InputStream
import java.util.UUID

@org.springframework.stereotype.Service
class PrintScriptService
    @Autowired
    constructor(
        private val formatterService: FormatterRulesService,
        private val linterRulesService: LinterRulesService,
    ) : Service {
        companion object {
            fun objectMapper(): ObjectMapper {
                val mapper = ObjectMapper()
                mapper.propertyNamingStrategy = PropertyNamingStrategies.UPPER_CAMEL_CASE
                return mapper
            }
        }

        override fun runScript(
            input: InputStream,
            version: String,
        ): Output {
            val executer = Executer()
            return executer.execute(input, version)
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
            return output
        }
    }
