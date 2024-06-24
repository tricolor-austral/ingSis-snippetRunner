package ingsis.tricolor.snippetrunner.service.interfaces

import ingsis.tricolor.snippetrunner.redis.dto.Snippet

interface RedisService {
    fun formatSnippet(snippet: Snippet): Snippet

    fun lintSnippet(snippet: Snippet): Snippet
}
