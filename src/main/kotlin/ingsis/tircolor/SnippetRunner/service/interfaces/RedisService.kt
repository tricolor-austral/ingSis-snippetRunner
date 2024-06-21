package ingsis.tircolor.snippetrunner.service.interfaces

import ingsis.tircolor.snippetrunner.redis.consumer.FormatProduct

interface RedisService {
    fun formatSnippet(snippet: FormatProduct): String
}
