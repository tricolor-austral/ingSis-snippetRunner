package ingsis.tircolor.SnippetRunner.model.dto

import java.io.InputStream
import java.util.*

data class SnippetFormatDto (
    val snippetId: UUID,
    val language: String,
    val version: String,
    val input: InputStream,
    val configPath: String
)