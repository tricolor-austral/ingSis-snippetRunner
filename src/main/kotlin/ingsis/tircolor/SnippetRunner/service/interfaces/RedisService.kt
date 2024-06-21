package ingsis.tircolor.snippetrunner.service.interfaces

import ingsis.tircolor.snippetrunner.redis.consumer.FormatProduct
import ingsis.tircolor.snippetrunner.redis.dto.Snippet

interface RedisService {
    fun formatSnippet(snippet: FormatProduct): Snippet
}
