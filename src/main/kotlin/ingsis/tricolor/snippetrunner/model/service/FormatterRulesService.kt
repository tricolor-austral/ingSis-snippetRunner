package ingsis.tricolor.snippetrunner.model.service

import ingsis.tricolor.snippetrunner.model.dto.FormatterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.FormatterRulesRepository
import ingsis.tricolor.snippetrunner.model.rules.FormatterRules
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class FormatterRulesService(
    @Autowired private var formatterRulesRepository: FormatterRulesRepository,
) {
    fun getFormatterRulesByUserId(
        userId: String,
        correlationId: UUID,
    ): FormatterRules {
        return formatterRulesRepository.findByUserId(userId).orElse(throw java.lang.RuntimeException("Not found"))
    }

    fun updateFormatterRules(
        formatterRules: FormatterRulesDto,
        userId: String,
    ): FormatterRulesDto {
        try {
            val rules = findOrCreateByUser(userId)
            rules.NewLinesBeforePrintln = formatterRules.NewLinesBeforePrintln
            rules.SpacesInAssignation = formatterRules.SpacesInAssignation
            rules.SpacesBeforeDeclaration = formatterRules.SpacesBeforeDeclaration
            rules.SpacesAfterDeclaration = formatterRules.SpacesAfterDeclaration
            formatterRulesRepository.save(rules)
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
        return formatterRulesRepository.findByUserId(userId).orElse(createUserById(userId))
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
