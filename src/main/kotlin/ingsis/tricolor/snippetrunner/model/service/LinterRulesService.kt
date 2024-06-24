package ingsis.tricolor.snippetrunner.model.service

import ingsis.tricolor.snippetrunner.model.dto.LinterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.FormatterRulesRepository
import ingsis.tricolor.snippetrunner.model.repository.LinterRulesRepository
import ingsis.tricolor.snippetrunner.model.rules.LinterRules
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LinterRulesService(@Autowired private var  linterRulesRepository: LinterRulesRepository) {

    fun getLinterRulesByUserId (userId: String, correlationId: UUID): LinterRules {
        return linterRulesRepository.findByUserId(userId)
    }
    fun updateLinterRules(linterRules: LinterRulesDto, userId: String): LinterRulesDto {
        try {
            var rules = linterRulesRepository.findByUserId(userId)

            if (rules == null) {
                // Si no se encuentra ninguna regla, creamos una nueva
                rules = LinterRules()
                rules.userId = userId
            }

            // Actualizamos los valores de las reglas con los nuevos valores
            rules.identifier = linterRules.identifier
            rules.printwithoutexpresion = linterRules.printwithoutexpresion
            rules.readinputwithoutexpresion = linterRules.readinputwithoutexpresion

            // Guardamos o actualizamos las reglas en la base de datos
            val savedRules = linterRulesRepository.save(rules)

            // Convertimos las reglas guardadas en DTO antes de devolverlas
            return LinterRulesDto(
                savedRules.userId ?: "",
                savedRules.identifier,
                savedRules.printwithoutexpresion,
                savedRules.readinputwithoutexpresion
            )
        } catch (e: Exception) {
            return LinterRulesDto("", "", false, false)
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