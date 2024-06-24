package ingsis.tricolor.snippetrunner.model.rules
import jakarta.persistence.*

import java.util.*

@Entity
class FormatterRules {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "userId")
    val userId: String? = null

    @Column(name = "NewLinesBeforePrintln")
    var NewLinesBeforePrintln: Int = 0

    @Column(name = "SpacesBeforeDeclaration")
    var SpacesBeforeDeclaration: Boolean = false;

    @Column(name = "SpacesAfterDeclaration")
    var SpacesAfterDeclaration: Boolean = false;

    @Column(name="SpacesInAssignation")
    var SpacesInAssignation: Boolean = false;


}