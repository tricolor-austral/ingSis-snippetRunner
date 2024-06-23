package ingsis.tricolor.snippetrunner.redis.dto

import java.util.UUID

data class Snippet(
    val id: String,
    val content: String,
    val correlationID: UUID,
)
