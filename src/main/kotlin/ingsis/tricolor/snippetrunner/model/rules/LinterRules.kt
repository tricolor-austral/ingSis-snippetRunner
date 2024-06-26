package ingsis.tricolor.snippetrunner.model.rules

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class LinterRules(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = UUID.randomUUID(),
    @Column(name = "userId")
    var userId: String? = null,
    @Column(name = "identifier")
    var identifier: String = "",
    @Column(name = "printwithoutexpresion")
    var printwithoutexpresion: Boolean = false,
    @Column(name = "readinputwithoutexpresion")
    var readinputwithoutexpresion: Boolean = false,
)
