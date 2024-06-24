package ingsis.tricolor.snippetrunner.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ingsis.tricolor.snippetrunner.model.dto.FormatFile
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
import java.util.*

@org.springframework.stereotype.Service
class PrintScriptService
    @Autowired
    constructor(
        private val formatterService: FormatterRulesService,
        private val linterRulesService: LinterRulesService,
    ) : Service {
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
            val objectMapper = jacksonObjectMapper()
            val linterDto = linterRulesService.getLinterRulesByUserId(userId, correlationId)
            val rulesFile = File("../model/files/$userId-linterRules.json")
            objectMapper.writeValue(rulesFile, linterDto)

            val defaultPath = "../model/files/$userId-linterRules.json"
            val linter = LinterExecuter()
            return linter.execute(input, version, defaultPath)
        }

        override fun format(
            input: InputStream,
            version: String,
            userId: String,
            correlationId: UUID,
        ): Output {
            val objectMapper = jacksonObjectMapper()
            val formatterRules = formatterService.getFormatterRulesByUserId(userId, correlationId)
            val formatterDto =
                FormatFile(
                    formatterRules.NewLinesBeforePrintln,
                    formatterRules.SpacesBeforeDeclaration,
                    formatterRules.SpacesAfterDeclaration,
                    formatterRules.SpacesInAssignation,
                )
            val rulesFile = File("src/main/kotlin/ingsis/tricolor/snippetrunner/model/files/$userId-formatterRules.json")
            objectMapper.writeValue(rulesFile, formatterDto)
            val defaultPath = "src/main/kotlin/ingsis/tricolor/snippetrunner/model/files/$userId-formatterRules.json"
            val formatter = FormatterExecuter()
            val output = formatter.execute(input, version, defaultPath)
// //        if (rulesFile.exists()) {
// //            rulesFile.delete()
// //        }
            return output
        }
    }
