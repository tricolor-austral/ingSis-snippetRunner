package ingsis.tricolor.snippetrunner.model.rules
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class FormatterRules(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(name = "userId", unique = true)
    val userId: String? = null,
    @Column(name = "NewLinesBeforePrintln")
    var NewLinesBeforePrintln: Int = 0,
    @Column(name = "SpacesBeforeDeclaration")
    var SpacesBeforeDeclaration: Boolean = false,
    @Column(name = "SpacesAfterDeclaration")
    var SpacesAfterDeclaration: Boolean = false,
    @Column(name = "SpacesInAssignation")
    var SpacesInAssignation: Boolean = false,
)
