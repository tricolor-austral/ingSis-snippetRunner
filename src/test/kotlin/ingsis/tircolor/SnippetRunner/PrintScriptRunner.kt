package ingsis.tircolor.SnippetRunner

import ingsis.tircolor.SnippetRunner.service.PrintScriptService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class PrintscriptExecutorTest {
    private val executor = PrintScriptService()

    private val inputBase = "src/test/resources/files"

    @Test
    fun `001 Interpret with empty InputStream`() {
        val result = executor.runScript(InputStream.nullInputStream(), "1.1")

        Assertions.assertEquals(0, result)
        Assertions.assertEquals(0, result)
    }

    @Test
    fun `002 Interpret file with single println`() {
        val inputFile = File("$inputBase/test001.txt")
        val input = FileInputStream(inputFile)

        val result = executor.runScript(input, "1.1")
        Assertions.assertEquals("Hello, World!", result)
    }
}