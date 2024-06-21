package ingsis.tircolor.snippetrunner.service

import org.springframework.stereotype.Service

@Service
class SnippetService {
    fun selectService(language: String): Service {
        return when (language) {
            "PrintScript" -> PrintScriptService() as Service
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}
