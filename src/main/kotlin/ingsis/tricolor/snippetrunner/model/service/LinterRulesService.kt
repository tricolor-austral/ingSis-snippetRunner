package ingsis.tricolor.snippetrunner.model.service

import ingsis.tricolor.snippetrunner.model.dto.LinterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.FormatterRulesRepository
import ingsis.tricolor.snippetrunner.model.repository.LinterRulesRepository
import ingsis.tricolor.snippetrunner.model.rules.LinterRules
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LinterRulesService {
    @Autowired
    private var  linterRulesRepository: LinterRulesRepository

    constructor(linterRulesRepository: LinterRulesRepository) {
        this.linterRulesRepository = linterRulesRepository
    }
    fun getLinterRulesByUserId (userId: String, correlationId: UUID): LinterRules {
        return linterRulesRepository.findByUserId(userId)
    }
    fun updateLinterRules(linterRules: LinterRules, userId: String) : LinterRulesDto {
        try {
            val rules = linterRulesRepository.findByUserId(userId)
            rules.identifier = linterRules.identifier
            rules.printwithoutexpresion = linterRules.printwithoutexpresion
            rules.readinputwithoutexpresion = linterRules.readinputwithoutexpresion
            return linterRulesRepository.save(rules) as LinterRulesDto
        } catch (e: Exception) {
            return linterRulesRepository.save(LinterRules()) as LinterRulesDto
        }
    }
}
//
//
//fun getFormatRules(
//    userId: String,
//    correlationId: UUID,
//): List<Rules>
//
//fun getLintRules(
//    userId: String,
//    correlationId: UUID,
//): List<Rules>
//data class Rules(
//    val id: String,
//    val name: String,
//    val isActive: Boolean,
//    val value: Any, // string number o null
//)