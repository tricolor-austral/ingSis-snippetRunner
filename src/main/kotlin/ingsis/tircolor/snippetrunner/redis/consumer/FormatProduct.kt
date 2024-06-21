package ingsis.tircolor.snippetrunner.redis.consumer

data class FormatProduct(
    val id: String,
    val snippet: String,
    val rules: List<String>, // poner lo que vaya en la regla
)
