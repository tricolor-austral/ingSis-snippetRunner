package ingsis.tricolor.snippetrunner.model.service

import ingsis.tricolor.snippetrunner.model.dto.FormatterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.FormatterRulesRepository
import ingsis.tricolor.snippetrunner.model.rules.FormatterRules
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class FormatterRulesService() {

    @Autowired
    private lateinit var  formatterRulesRepository: FormatterRulesRepository

    constructor(formatterRulesRepository: FormatterRulesRepository) : this() {
        this.formatterRulesRepository = formatterRulesRepository
    }
    fun getFormatterRulesByUserId (userId: String, correlationId: UUID): FormatterRules {
        return formatterRulesRepository.findByUserId(userId)
    }
    fun updateFormatterRules(formatterRules: FormatterRulesDto, userId: String) : FormatterRulesDto {
        try {
            val rules = formatterRulesRepository.findByUserId(userId)
            rules.NewLinesBeforePrintln = formatterRules.NewLinesBeforePrintln
            rules.SpacesInAssignation = formatterRules.SpacesInAssignation
            rules.SpacesBeforeDeclaration = formatterRules.SpacesBeforeDeclaration
            rules.SpacesAfterDeclaration = formatterRules.SpacesAfterDeclaration
            return formatterRulesRepository.save(rules) as FormatterRulesDto
        }
        catch (e: Exception){
            return formatterRulesRepository.save(FormatterRules()) as FormatterRulesDto
        }
    }
}