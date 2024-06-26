package ingsis.tricolor.snippetrunner.model.repository

import ingsis.tricolor.snippetrunner.model.rules.FormatterRules
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface FormatterRulesRepository : JpaRepository<FormatterRules, UUID> {
    fun findByUserId(userId: String): Optional<FormatterRules>
}
