package ingsis.tricolor.snippetrunner.service

import org.springframework.stereotype.Service

@Service
class SnippetService {
    fun selectService(language: String): ingsis.tricolor.snippetrunner.service.interfaces.Service {
        return when (language) {
            "PrintScript" -> PrintScriptService()
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}
