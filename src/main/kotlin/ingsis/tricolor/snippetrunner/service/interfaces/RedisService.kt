package ingsis.tricolor.snippetrunner.service.interfaces

import ingsis.tricolor.snippetrunner.redis.consumer.FormatProduct
import ingsis.tricolor.snippetrunner.redis.dto.Snippet

interface RedisService {
    fun formatSnippet(snippet: FormatProduct): Snippet
}
