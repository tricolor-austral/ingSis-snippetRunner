package ingsis.tricolor.snippetrunner.redis.dto

data class Rule(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val value: Any, // string number o null
)
