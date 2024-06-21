package ingsis.tircolor.SnippetRunner.service

import ingsis.tircolor.SnippetRunner.model.dto.SnippetRunnerDTO
import org.example.Output
import org.example.executer.Executer
import org.example.executer.FormatterExecuter
import org.example.executer.LinterExecuter
import java.io.InputStream

import org.example.staticCodeeAnalyzer.SCAOutput

class PrintScriptService : Service {
    override fun runScript(input: InputStream, version: String) : Output {
        val executer = Executer()
        return executer.execute(input, version)
    }

    override fun runLinter(input: InputStream, version: String, configPath: String): MutableList<SCAOutput> {
        val linter = LinterExecuter()
        return linter.execute(input, version, configPath)

    }

    override fun format(input: InputStream, version: String, configPath: String): Output {
        val formatter = FormatterExecuter()
        return formatter.execute(input, version, configPath)
    }

}