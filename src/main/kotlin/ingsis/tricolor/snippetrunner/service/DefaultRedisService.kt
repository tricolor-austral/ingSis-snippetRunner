package ingsis.tricolor.snippetrunner.service

import ingsis.tricolor.snippetrunner.redis.dto.Snippet
import ingsis.tricolor.snippetrunner.service.interfaces.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class DefaultRedisService
    @Autowired
    constructor(
        private val snippetService: PrintScriptService,
        @Value("\${permission.url}") private val permissionUrl: String,
    ) : RedisService {
        val operationsApi = WebClient.builder().baseUrl("http://$permissionUrl").build()

        override fun formatSnippet(snippet: Snippet): Snippet {
            // TODO: tengo que formatear el snippet y mandarselo a el operations (snippetholder) para que lo guarde
            println("Estoy formateando un snippet")
            return snippet
        }

        override fun lintSnippet(snippet: Snippet): Snippet {
            // TODO: tengo que lintear el snippet y mandarselo a el operations (snippetholder) para que lo guarde
            println("Estoy linteando un snippet")
            return snippet
        }
    }
