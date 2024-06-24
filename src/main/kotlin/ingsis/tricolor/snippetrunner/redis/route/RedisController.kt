package ingsis.tricolor.snippetrunner.redis.route

import ingsis.tricolor.snippetrunner.model.dto.FormatterRulesDto
import ingsis.tricolor.snippetrunner.model.dto.LinterRulesDto
import ingsis.tricolor.snippetrunner.model.service.FormatterRulesService
import ingsis.tricolor.snippetrunner.model.service.LinterRulesService
import ingsis.tricolor.snippetrunner.redis.dto.ChangeRulesDto
import ingsis.tricolor.snippetrunner.redis.dto.Snippet
import ingsis.tricolor.snippetrunner.redis.producer.SnippetFormattedProducer
import ingsis.tricolor.snippetrunner.redis.producer.SnippetLintProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("redis")
class RedisController
    @Autowired
    constructor(
        private val formatProducer: SnippetFormattedProducer,
        private val lintProducer: SnippetLintProducer,
        private val formatterService: FormatterRulesService,
        private val linterRulesService: LinterRulesService,

        ) {
    @PutMapping("format")
    suspend fun changeAndFormatRules(
        @RequestBody data: ChangeRulesDto,
    ) {
        val newLinesBeforePrint = data.rules.find { it.name == "NewLinesBeforePrintln" }?.value as? Int ?: 0
        val spaceBeforeDecl = data.rules.find { it.name == "SpacesBeforeDeclaration" }?.value as? Boolean ?: false
        val spaceAfterDecl = data.rules.find { it.name == "SpacesAfterDeclaration" }?.value as? Boolean ?: false
        val spaceAfterAssig = data.rules.find { it.name == "SpacesInAssignation" }?.value as? Boolean ?: false

        val formatterDto = FormatterRulesDto(
            data.userId,
            newLinesBeforePrint,
            spaceBeforeDecl,
            spaceAfterDecl,
            spaceAfterAssig
        )
        formatterService.updateFormatterRules(formatterDto, data.userId)

        data.snippets.forEach {
            val snippet = Snippet(data.userId, it.input, data.correlationId)
            formatProducer.publishEvent(snippet)
        }
    }

        @PutMapping("lint")
        suspend fun changeAndLintRules(
            @RequestBody data: ChangeRulesDto,
        ) {
            val identifier = data.rules.find { it.name == "identifier" }?.value as? String ?: ""
            val printWithoutExpresion = data.rules.find { it.name == "printwithoutexpresion" }?.value as? Boolean ?: false
            val readInputWithoutExpresion = data.rules.find { it.name == "readinputwithoutexpresion" }?.value as? Boolean ?: false

            val linterDto = LinterRulesDto(
                data.userId,
                identifier,
                printWithoutExpresion,
                readInputWithoutExpresion
            )
            linterRulesService.updateLinterRules(linterDto, data.userId)
            data.snippets.map {
                val snippet = Snippet(data.userId, it.input, data.correlationId)
                lintProducer.publishEvent(snippet)
            }
        }
    }
