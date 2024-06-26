package ingsis.tricolor.snippetrunner.redis.dto

import java.util.UUID

data class ExecutionDataDto(
    val correlationId: UUID,
    val snippetId: String,
    val language: String,
    val version: String,
    val input: String,
)
