package ingsis.tricolor.snippetrunner.model.rules
import jakarta.persistence.*

import java.util.*

@Entity
class FormatterRules {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null
    @Column(name =  "active")
    val active: Boolean = false

    @Column(name = "NewLinesBeforePrintln")
    val NewLinesBeforePrintln: Int = 0

    @Column(name = "SpacesBeforeDeclaration")
    val SpacesBeforeDeclaration: Boolean = false;

    @Column(name = "SpacesAfterDeclaration")
    val SpacesAfterDeclaration: Boolean = false;


}