package ingsis.tricolor.snippetrunner.service.interfaces

import org.example.Output
import org.example.staticCodeeAnalyzer.SCAOutput
import java.io.InputStream
import java.util.UUID

interface Service {
    fun runScript(
        input: InputStream,
        version: String,
    ): Output

    fun runLinter(
        input: InputStream,
        version: String,
        userId: String,
        correlationId: UUID,
    ): MutableList<SCAOutput>

    fun format(
        snippetId: String,
        input: InputStream,
        version: String,
        userId: String,
        correlationId: UUID,
    ): Output

    fun test(
        input: String,
        output: List<String>,
        snippet: String,
        envVars: String
    ): String
}
