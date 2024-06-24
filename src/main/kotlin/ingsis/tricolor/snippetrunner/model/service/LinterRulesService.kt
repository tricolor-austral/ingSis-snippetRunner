package ingsis.tricolor.snippetrunner.model.service

import ingsis.tricolor.snippetrunner.model.dto.LinterRulesDto
import ingsis.tricolor.snippetrunner.model.repository.LinterRulesRepository
import ingsis.tricolor.snippetrunner.model.rules.LinterRules
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LinterRulesService(
    @Autowired private var linterRulesRepository: LinterRulesRepository,
) {
    fun getLinterRulesByUserId(
        userId: String,
        correlationId: UUID,
    ): LinterRules {
        return findOrCreateByUser(userId)
    }

    fun updateLinterRules(
        linterRules: LinterRulesDto,
        userId: String,
    ): LinterRulesDto {
        try {
            var rules = findOrCreateByUser(userId)

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
                savedRules.readinputwithoutexpresion,
            )
        } catch (e: Exception) {
            return LinterRulesDto("", "", false, false)
        }
    }

    private fun findOrCreateByUser(userId: String): LinterRules {
        val rules = linterRulesRepository.findByUserId(userId).orElse(null)
        if (rules == null) {
            println("User not found")
            return createUserById(userId)
        }
        return rules
    }

    private fun createUserById(userId: String): LinterRules {
        val format =
            LinterRules(
                userId = userId,
                identifier = "camelcase",
                printwithoutexpresion = false,
                readinputwithoutexpresion = false,
            )
        return linterRulesRepository.save(format)
    }
}
