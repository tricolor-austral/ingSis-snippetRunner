package ingsis.tricolor.snippetrunner.model.dto

import java.util.*

data class SnippetRunnerDTO(
    val correlationId: UUID,
    val snippetId: String,
    val language: String,
    val version: String,
    val input: String,
    val userId: String,
)
