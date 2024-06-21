package ingsis.tircolor.SnippetRunner.service

import org.springframework.stereotype.Service

@Service
class SnippetService {

    fun selectService(language: String): ingsis.tircolor.SnippetRunner.service.Service {
        return when (language) {
            "PrintScript" -> PrintScriptService()
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}