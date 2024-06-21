package ingsis.tircolor.SnippetRunner.service

import ingsis.tircolor.SnippetRunner.model.dto.SnippetRunnerDTO
import org.example.Output
import java.io.InputStream

interface Service {
    fun runScript(input: InputStream, version: String): Output
    fun runLinter(input: InputStream, version: String): Output
    fun format(input: InputStream, version: String, configPath: String): Output

}