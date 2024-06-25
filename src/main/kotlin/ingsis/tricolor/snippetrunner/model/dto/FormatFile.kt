package ingsis.tricolor.snippetrunner.model.dto

data class FormatFile(
    val NewLinesBeforePrintln: Int,
    val SpacesBeforeDeclaration: Boolean,
    val SpacesAfterDeclaration: Boolean,
    val SpacesInAssignation: Boolean,
)
