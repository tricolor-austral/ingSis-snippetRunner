package ingsis.tricolor.snippetrunner.model.dto

import java.util.*

data class TestDto (
    val input: String,
    val snippet: String,
    val output: List<String>,
    val envVars: String
)