package ingsis.tricolor.snippetrunner.model.rules
import jakarta.persistence.*

import java.util.*

@Entity
class LinterRules {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "userId")
    var userId: String? = null

    @Column(name = "identifier")
    var identifier: String = ""

    @Column(name = "printwithoutexpresion")
    var printwithoutexpresion: Boolean= false

    @Column(name = "readinputwithoutexpresion")
    var readinputwithoutexpresion: Boolean= false
}