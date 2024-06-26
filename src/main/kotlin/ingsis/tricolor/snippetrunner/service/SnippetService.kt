package ingsis.tricolor.snippetrunner.service

import org.springframework.stereotype.Service
import org.springframework.web.context.WebApplicationContext

@Service
class SnippetService(private val applicationContext: WebApplicationContext) {
    fun selectService(language: String): ingsis.tricolor.snippetrunner.service.interfaces.Service {
        return when (language) {
            "PrintScript" -> applicationContext.getBean(PrintScriptService::class.java)
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}
