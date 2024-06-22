package ingsis.tricolor.snippetrunner.controller

import ingsis.tricolor.snippetrunner.model.dto.SnippetFormatAndLinterDto
import ingsis.tricolor.snippetrunner.model.dto.SnippetOutputDto
import ingsis.tricolor.snippetrunner.model.dto.SnippetRunnerDTO
import ingsis.tricolor.snippetrunner.service.SnippetService
import org.example.Output
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.io.InputStream

@RestController
class SnippetController(private val snippetService: SnippetService) {
    @PostMapping("/run")
    fun runSnippet(
        @RequestHeader("Authorization") token: String,
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
        @RequestBody snippetRunnerDTO: SnippetFormatAndLinterDto,
    ): ResponseEntity<SnippetOutputDto> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val inputStream = ByteArrayInputStream(snippetRunnerDTO.input.toByteArray())
        val output = languageService.format(inputStream, snippetRunnerDTO.version, snippetRunnerDTO.configPath)
        val snippetOutput = SnippetOutputDto(output.string, snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }

    @PostMapping("/lint")
    fun runLinter(
        @RequestBody snippetRunnerDTO: SnippetFormatAndLinterDto,
    ): ResponseEntity<SnippetOutputDto> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val inputStream = ByteArrayInputStream(snippetRunnerDTO.input.toByteArray())
        val output = languageService.runLinter(inputStream, snippetRunnerDTO.version, snippetRunnerDTO.configPath)
        val brokenRules: MutableList<String> = output.flatMap { it.getBrokenRules() }.toMutableList()
        val snippetOutput = SnippetOutputDto(brokenRules.joinToString("\n"), snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }
}
