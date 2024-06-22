package ingsis.tricolor.snippetrunner.model.dto

import java.io.InputStream
import java.util.*

data class SnippetFormatAndLinterDto(
    val correlationId: UUID,
    val snippetId: UUID,
    val language: String,
    val version: String,
    val input: String,
    val configPath: String,
)
