package ingsis.tricolor.snippetrunner.model.service

import ingsis.tricolor.snippetrunner.model.dto.FormatterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.FormatterRulesRepository
import ingsis.tricolor.snippetrunner.model.rules.FormatterRules
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FormatterRulesService(
    @Autowired private var formatterRulesRepository: FormatterRulesRepository,
) {
    fun getFormatterRulesByUserId(
        userId: String,
        correlationId: UUID,
    ): FormatterRules {
        println("userId: $userId")
        return findOrCreateByUser(userId)
    }

    fun updateFormatterRules(
        formatterRules: FormatterRulesDto,
        userId: String,
    ): FormatterRulesDto {
        try {
            println("userId: $userId")
            val rules = findOrCreateByUser(userId)
            print("old rules: ${rules}")
            rules.NewLinesBeforePrintln = formatterRules.NewLinesBeforePrintln
            rules.SpacesInAssignation = formatterRules.SpacesInAssignation
            rules.SpacesBeforeDeclaration = formatterRules.SpacesBeforeDeclaration
            rules.SpacesAfterDeclaration = formatterRules.SpacesAfterDeclaration
            formatterRulesRepository.save(rules)
            println(rules)

            return FormatterRulesDto(
                userId,
                rules.NewLinesBeforePrintln,
                rules.SpacesInAssignation,
                rules.SpacesBeforeDeclaration,
                rules.SpacesAfterDeclaration,
            )
        } catch (e: Exception) {
            throw RuntimeException("Could not save rules")
        }
    }

    private fun findOrCreateByUser(userId: String): FormatterRules {
        val rules = formatterRulesRepository.findByUserId(userId).orElse(null)
        println("rules: $rules")
        if (rules == null) {
            println("User not found")
            return createUserById(userId)
        }
        return rules
    }

    private fun createUserById(userId: String): FormatterRules {
        val format =
            FormatterRules(
                userId = userId,
                NewLinesBeforePrintln = 0,
                SpacesBeforeDeclaration = false,
                SpacesAfterDeclaration = false,
                SpacesInAssignation = false,
            )
        return formatterRulesRepository.save(format)
    }
}
