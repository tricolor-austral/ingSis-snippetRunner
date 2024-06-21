package ingsis.tircolor.snippetrunner.service

import ingsis.tircolor.snippetrunner.redis.consumer.FormatProduct
import ingsis.tircolor.snippetrunner.redis.dto.Snippet
import ingsis.tircolor.snippetrunner.service.interfaces.RedisService
import org.springframework.web.reactive.function.client.WebClient
import java.io.ByteArrayInputStream

class DefaultRedisService(private val snippetService: PrintScriptService) : RedisService {
    val operationsApi = WebClient.builder().baseUrl("http://\${permission.url}").build()

    override fun formatSnippet(snippet: FormatProduct): Snippet {
        // TODO: tengo que formatear el snippet y mandarselo a el operations (snippetholder) para que lo guarde
        val stream = ByteArrayInputStream(snippet.snippet.toByteArray(Charsets.UTF_8))
        val formattedSnippet = snippetService.format(stream, "1.1", "Aca le pasaria las reglas en ves del path")
        val bodyValue = Snippet(snippet.id, formattedSnippet.toString())
        operationsApi.post()
            .uri("/update-snippet-runner")
            .bodyValue(bodyValue)
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw RuntimeException("Couldn't save snippet")

        return bodyValue
    }
}
