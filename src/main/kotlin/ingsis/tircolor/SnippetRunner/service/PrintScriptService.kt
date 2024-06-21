package ingsis.tircolor.SnippetRunner.service

import ingsis.tircolor.SnippetRunner.model.dto.SnippetRunnerDTO
import org.example.Output
import java.io.InputStream
import org.example.parser.Parser
import org.example.executer.Executer
import org.example.executer.FormatterExecuter
import org.example.splittingStrategy.StrategyMapper

class PrintScriptService : Service {
    override fun runScript(input: InputStream, version: String) : Output {
        val executer: Executer = Executer()
        return executer.execute(input, version)
    }

    override fun runLinter(input: InputStream, version: String): Output {
        TODO("Not yet implemented")
    }

    override fun format(input: InputStream, version: String, configPath: String): Output {
        val formatter = FormatterExecuter()
        return formatter.execute(input, version, configPath)
    }

}