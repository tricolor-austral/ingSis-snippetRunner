@file:Suppress("ktlint:standard:filename")

package ingsis.tircolor.snippetrunner

import ingsis.tircolor.snippetrunner.service.PrintScriptService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class PrintscriptExecutorTest {
    private val executor = PrintScriptService()
    private val fileFounder = "src/test/resources/files"
    private val ruleFounder = "src/test/resources/rules"

    @Test
    fun `001 empty`() {
        val result = executor.runScript(InputStream.nullInputStream(), "1.1")

        Assertions.assertEquals("", result.string)
    }

    @Test
    fun `002 test hello world`() {
        val inputFile = File("$fileFounder/test001.txt")
        val input = FileInputStream(inputFile)

        val result = executor.runScript(input, "1.1")
        assertEquals("Hello, World!", result.string)
    }

    @Test
    fun `003 test long file`() {
        val inputFile = File("$fileFounder/test002.txt")
        val input = FileInputStream(inputFile)

        val result = executor.runScript(input, "1.1")
        assertEquals("hello world\n10\noutside of conditional\n", result.string)
    }

    @Test
    fun `004 test linter with my rules`() {
        val inputFile = File("$fileFounder/test003.txt")
        val configjson = "$ruleFounder/MyRules.json"
        val input = FileInputStream(inputFile)
        val result = executor.runLinter(input, "1.1", configjson)
        val brokenRules: MutableList<String> = result.flatMap { it.getBrokenRules() }.toMutableList()
        assertEquals("Println must not be called with an expression at line 0", brokenRules.joinToString("\n"))
    }
}
