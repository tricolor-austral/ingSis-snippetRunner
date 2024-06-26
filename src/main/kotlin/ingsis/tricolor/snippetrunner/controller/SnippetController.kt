package ingsis.tricolor.snippetrunner.controller

import OutputRulesDto
import com.fasterxml.jackson.databind.ObjectMapper
import ingsis.tricolor.snippetrunner.model.dto.SnippetOutputDto
import ingsis.tricolor.snippetrunner.model.dto.SnippetRunnerDTO
import ingsis.tricolor.snippetrunner.model.service.FormatterRulesService
import ingsis.tricolor.snippetrunner.model.service.LinterRulesService
import ingsis.tricolor.snippetrunner.redis.dto.Rule
import ingsis.tricolor.snippetrunner.service.SnippetService
import org.example.Output
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*

@RestController
class SnippetController(
    private val snippetService: SnippetService,
    private val linterRulesService: LinterRulesService,
    private val formaterRulesService: FormatterRulesService
    ) {
    @PostMapping("/run")
    fun runSnippet(
        @RequestBody snippetRunnerDTO: SnippetRunnerDTO,
    ): ResponseEntity<SnippetOutputDto> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val inputStream = ByteArrayInputStream(snippetRunnerDTO.input.toByteArray())
        val output: Output = languageService.runScript(inputStream, snippetRunnerDTO.version)
        val snippetOutput = SnippetOutputDto(output.string, snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }

    @PostMapping("/format")
    fun formatSnippet(
        @RequestBody snippetRunnerDTO: SnippetRunnerDTO,
    ): ResponseEntity<SnippetOutputDto> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val inputStream = ByteArrayInputStream(snippetRunnerDTO.input.toByteArray())
        val output = languageService.format(inputStream, snippetRunnerDTO.version, snippetRunnerDTO.userId, snippetRunnerDTO.correlationId)
        val snippetOutput = SnippetOutputDto(output.string, snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }

    @PostMapping("/lint")
    fun runLinter(
        @RequestBody snippetRunnerDTO: SnippetRunnerDTO,
    ): ResponseEntity<SnippetOutputDto> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val inputStream = ByteArrayInputStream(snippetRunnerDTO.input.toByteArray())
        val output =
            languageService.runLinter(
                inputStream,
                snippetRunnerDTO.version,
                snippetRunnerDTO.userId,
                snippetRunnerDTO.correlationId,
            )
        val brokenRules: MutableList<String> = output.flatMap { it.getBrokenRules() }.toMutableList()
        val snippetOutput = SnippetOutputDto(brokenRules.joinToString("\n"), snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }
    @GetMapping("/format/{userId}")
    fun getLinterRules(@PathVariable userId: String, @RequestHeader("Correlation-id") correlationId: UUID): ResponseEntity<List<Rule>> {
        val formatterRules = formaterRulesService.getFormatterRulesByUserId(userId, correlationId)
        val rulesList = mutableListOf<Rule>()

    rulesList.add(Rule(id = "1", name = "NewLinesBeforePrintln", isActive = true, value = formatterRules.NewLinesBeforePrintln))
    rulesList.add(Rule(id = "2", name = "SpacesInAssignation", isActive = true, value = formatterRules.SpacesInAssignation))
    rulesList.add(Rule(id = "3", name = "SpacesAfterDeclaration", isActive = true, value = formatterRules.SpacesAfterDeclaration))
    rulesList.add(Rule(id = "4", name = "SpacesBeforeDeclaration", isActive = true, value = formatterRules.SpacesBeforeDeclaration))

    return ResponseEntity.ok(rulesList)
}
    @GetMapping("/linter/{userId}")
    fun getFormatterRules(@PathVariable userId: String, @RequestHeader("Correlation-id") correlationId: UUID): ResponseEntity<List<Rule>> {
        val linterRules = linterRulesService.getLinterRulesByUserId(userId, correlationId)
        val rulesList = mutableListOf<Rule>()

        rulesList.add(Rule(id = "1", name = "identifier", isActive = true, value = linterRules.identifier))
        rulesList.add(Rule(id = "2", name = "printwithoutexpresion", isActive = true, value = linterRules.printwithoutexpresion))
        rulesList.add(Rule(id = "3", name = "readinputwithoutexpresion", isActive = true, value = linterRules.readinputwithoutexpresion))

        return ResponseEntity.ok(rulesList)
    }
}
