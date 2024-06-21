package ingsis.tircolor.snippetrunner.service

import org.springframework.stereotype.Service

@Service
class SnippetService {
    fun selectService(language: String): ingsis.tircolor.snippetrunner.service.interfaces.Service {
        return when (language) {
            "PrintScript" -> PrintScriptService()
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}
