package ingsis.tricolor.snippetrunner.model.repository
import ingsis.tricolor.snippetrunner.model.rules.LinterRules
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface LinterRulesRepository : JpaRepository<LinterRules, UUID> {
    fun findByUserId(userId: String): Optional<LinterRules>
}
