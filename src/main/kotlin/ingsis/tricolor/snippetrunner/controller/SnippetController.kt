package ingsis.tricolor.snippetrunner.controller

import com.fasterxml.jackson.databind.ObjectMapper
import ingsis.tricolor.snippetrunner.model.dto.SnippetOutputDto
import ingsis.tricolor.snippetrunner.model.dto.SnippetRunnerDTO
import ingsis.tricolor.snippetrunner.service.SnippetService
import org.example.Output
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream

@RestController
class SnippetController(
    private val snippetService: SnippetService,
    private val jacksonObjectMapper: ObjectMapper,
    objectMapper: ObjectMapper,
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
        val defaultPath = "/Users/usuario/Desktop/Austral/4-primer-cuatri/Ing-sistemas/ingSis-2/ingSis-snippetRunner/src/main/kotlin/ingsis/tricolor/snippetrunner/model/dto/formatterRules.json"
        val output = languageService.format(inputStream, snippetRunnerDTO.version, defaultPath)
        val snippetOutput = SnippetOutputDto(output.string, snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }

    @PostMapping("/lint")
    fun runLinter(
        @RequestBody snippetRunnerDTO: SnippetRunnerDTO,
    ): ResponseEntity<SnippetOutputDto> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val inputStream = ByteArrayInputStream(snippetRunnerDTO.input.toByteArray())
        val defaultConfigPath = "/Users/usuario/Desktop/Austral/4-primer-cuatri/Ing-sistemas/ingSis-2/ingSis-snippetRunner/src/main/kotlin/ingsis/tricolor/snippetrunner/model/dto/linterRules.json"
        val output = languageService.runLinter(inputStream, snippetRunnerDTO.version, defaultConfigPath)
        val brokenRules: MutableList<String> = output.flatMap { it.getBrokenRules() }.toMutableList()
        val snippetOutput = SnippetOutputDto(brokenRules.joinToString("\n"), snippetRunnerDTO.correlationId, snippetRunnerDTO.snippetId)
        return ResponseEntity(snippetOutput, HttpStatus.OK)
    }
}
