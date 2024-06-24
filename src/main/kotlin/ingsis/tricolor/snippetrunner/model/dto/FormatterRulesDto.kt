package ingsis.tricolor.snippetrunner.model.dto

data class FormatterRulesDto (
    val userId: String,
    val NewLinesBeforePrintln: Int,
    val SpacesBeforeDeclaration: Boolean,
    val SpacesAfterDeclaration: Boolean,
    val SpacesInAssignation: Boolean
)