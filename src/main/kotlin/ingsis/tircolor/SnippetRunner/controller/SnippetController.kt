package ingsis.tircolor.SnippetRunner.controller

import ingsis.tircolor.SnippetRunner.model.dto.SnippetFormatDto
import ingsis.tircolor.SnippetRunner.model.dto.SnippetRunnerDTO
import ingsis.tircolor.SnippetRunner.service.SnippetService
import org.example.Output
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream

@RestController
class SnippetController(private val snippetService : SnippetService) {
    @PostMapping("/run")
    fun runSnippet(@RequestHeader ("Authorization") token: String ,@RequestBody snippetRunnerDTO: SnippetRunnerDTO) : ResponseEntity<String> {
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val output: Output = languageService.runScript(snippetRunnerDTO.input , snippetRunnerDTO.version)
        return ResponseEntity(output.string, HttpStatus.OK)
    }
    @PostMapping("/format")
    fun formatSnippet(@RequestBody snippetRunnerDTO: SnippetFormatDto): ResponseEntity<String>{
        val languageService = snippetService.selectService(snippetRunnerDTO.language)
        val output = languageService.format(snippetRunnerDTO.input, snippetRunnerDTO.version, snippetRunnerDTO.configPath)
        return ResponseEntity(output.string, HttpStatus.OK)

    }
    fun runLinter (language: String, script: InputStream, version: String){

    }
}