package ingsis.tricolor.snippetrunner.model.dto

import java.io.InputStream
import java.util.*

data class SnippetRunnerDTO(
    val correlationId: UUID,
    val snippetId: UUID,
    val language: String,
    val version: String,
    val input: String,
)
