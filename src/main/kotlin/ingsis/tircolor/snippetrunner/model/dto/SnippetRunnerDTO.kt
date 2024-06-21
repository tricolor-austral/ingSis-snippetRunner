package ingsis.tircolor.SnippetRunner.model.dto

import java.io.InputStream
import java.util.*

data class SnippetRunnerDTO (
    val snippetId: UUID,
    val language: String,
    val version: String,
    val input: InputStream
    )