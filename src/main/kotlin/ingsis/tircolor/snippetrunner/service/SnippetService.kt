package ingsis.tircolor.snippetrunner.service

import ingsis.tircolor.SnippetRunner.service.PrintScriptService
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
