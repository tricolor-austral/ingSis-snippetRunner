package ingsis.tricolor.snippetrunner.redis.consumer

data class FormatProduct(
    val userId: String,
    val snippetId: String,
    val snippet: String,
)
