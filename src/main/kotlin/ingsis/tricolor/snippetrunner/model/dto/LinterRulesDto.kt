package ingsis.tricolor.snippetrunner.model.dto

data class LinterRulesDto(
    val userId: String,
    val identifier: String,
    val printwithoutexpresion: Boolean,
    val readinputwithoutexpresion: Boolean,
)
