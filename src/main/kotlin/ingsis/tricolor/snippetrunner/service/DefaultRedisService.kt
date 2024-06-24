package ingsis.tricolor.snippetrunner.service

import ingsis.tricolor.snippetrunner.redis.dto.Snippet
import ingsis.tricolor.snippetrunner.service.interfaces.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.ByteArrayInputStream

@Service
class DefaultRedisService
    @Autowired
    constructor(
        private val snippetService: PrintScriptService,
        @Value("\${permission.url}") private val permissionUrl: String,
    ) : RedisService {
        val operationsApi = WebClient.builder().baseUrl("http://$permissionUrl").build()

        override fun formatSnippet(snippet: Snippet): Snippet {
            val defaultPath =
                "src/main/kotlin/ingsis/tricolor/snippetrunner/model/files/${snippet.userId}-formatterRules.json"
            val snippetFormateado =
                snippetService.format(ByteArrayInputStream(snippet.content.toByteArray()), "1.1", snippet.userId, snippet.correlationID)
            println("Estoy formateando un snippet")
            val outputSnippet = Snippet(snippet.id, snippetFormateado.string, snippet.userId, snippet.correlationID)
            return outputSnippet
        }

        override fun lintSnippet(snippet: Snippet): Snippet {
            val defaultPath =
                "src/main/kotlin/ingsis/tricolor/snippetrunner/model/files/${snippet.id}-linterRules.json"
            println("Estoy linteando un snippet")
            val snippetLinteado =
                snippetService.runLinter(
                    ByteArrayInputStream(snippet.content.toByteArray()),
                    "1.1",
                    defaultPath,
                    snippet.correlationID,
                )
            val brokenRules: MutableList<String> = snippetLinteado.flatMap { it.getBrokenRules() }.toMutableList()
            val outputSnippet = Snippet(snippet.id, brokenRules.joinToString("\n"), snippet.userId, snippet.correlationID)
            return outputSnippet
        }
    }
