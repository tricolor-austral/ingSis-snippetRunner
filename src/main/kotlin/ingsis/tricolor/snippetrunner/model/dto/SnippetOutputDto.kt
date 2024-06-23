package ingsis.tricolor.snippetrunner.model.dto

import java.util.*

data class SnippetOutputDto (
    val snippet: String,
    val correlationId: UUID,
    val snippetId: UUID
)