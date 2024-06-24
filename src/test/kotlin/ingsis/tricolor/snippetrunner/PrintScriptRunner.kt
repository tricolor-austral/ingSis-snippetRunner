import ingsis.tricolor.snippetrunner.model.dto.LinterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.LinterRulesRepository
import ingsis.tricolor.snippetrunner.model.service.FormatterRulesService
import ingsis.tricolor.snippetrunner.model.service.LinterRulesService
import ingsis.tricolor.snippetrunner.service.PrintScriptService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

@ExtendWith(MockitoExtension::class)
class PrintscriptExecutorTest {
    @Mock
    lateinit var formatterService: FormatterRulesService

    @Mock
    lateinit var linterService: LinterRulesService

    @Mock
    lateinit var linterRulesRepository: LinterRulesRepository

    @InjectMocks
    lateinit var executor: PrintScriptService

    private val fileFounder = "src/test/resources/files"

    @Test
    fun `001 empty`() {
        val result = executor.runScript(InputStream.nullInputStream(), "1.1")
        assertEquals("", result.string)
    }

    @Test
    fun `002 test hello world`() {
        val inputFile = File("$fileFounder/test001.txt")
        val input = FileInputStream(inputFile)

        val result = executor.runScript(input, "1.1")
        assertEquals("Hello, World!\n", result.string)
    }

    @Test
    fun `003 test long file`() {
        val inputFile = File("$fileFounder/test002.txt")
        val input = FileInputStream(inputFile)

        val result = executor.runScript(input, "1.1")
        assertEquals("hello world\n10\noutside of conditional\n", result.string)
    }

    @Test
    fun `creating linter rules and getting linterRules`() {
        val correlationId = UUID.randomUUID()
        val userId = "1"
        val linterRules = LinterRulesDto(userId, "camelcase", true, true)

        val returnDto = linterService.updateLinterRules(linterRules, userId)
        assertEquals(linterRules, returnDto)
    }
//
//    @Test
//    fun `004 test linter with my rules`() {
//        val inputFile = File("$fileFounder/test003.txt")
//        val input = FileInputStream(inputFile)
//        val correlationId = UUID.randomUUID()
//
//        // Mock del comportamiento del repositorio
//        whenever(linterRulesRepository.findByUserId("10"))
//            .thenReturn(LinterRules("10", "camelcase", true, true))
//
//        val result = executor.runLinter(input, "1.1", "10", correlationId)
//        val brokenRules = result.flatMap { it.getBrokenRules() }.toMutableList()
//        assertEquals("Println must not be called with an expression at line 0", brokenRules.joinToString("\n"))
//    }
}
