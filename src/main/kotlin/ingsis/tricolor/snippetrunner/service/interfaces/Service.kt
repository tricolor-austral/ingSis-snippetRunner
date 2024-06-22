package ingsis.tricolor.snippetrunner.service.interfaces

import org.example.Output
import org.example.staticCodeeAnalyzer.SCAOutput
import java.io.InputStream

interface Service {
    fun runScript(
        input: InputStream,
        version: String,
    ): Output

    fun runLinter(
        input: InputStream,
        version: String,
        configPath: String,
    ): MutableList<SCAOutput>

    fun format(
        input: InputStream,
        version: String,
        configPath: String,
    ): Output
}
