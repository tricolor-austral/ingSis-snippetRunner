package ingsis.tricolor.snippetrunner.service

import ingsis.tricolor.snippetrunner.redis.consumer.FormatProduct
import ingsis.tricolor.snippetrunner.redis.dto.Snippet
import ingsis.tricolor.snippetrunner.service.interfaces.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.ByteArrayInputStream

@Service
class DefaultRedisService
    @Autowired
    constructor(private val snippetService: PrintScriptService) : RedisService {
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