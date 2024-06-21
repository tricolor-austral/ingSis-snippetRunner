@file:Suppress("ktlint:standard:no-wildcard-imports")

package ingsis.tircolor.snippetrunner.model.dto

import java.io.InputStream
import java.util.*

data class SnippetRunnerDTO(
    val snippetId: UUID,
    val language: String,
    val version: String,
    val input: InputStream,
)
