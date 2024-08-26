package ar.edu.unsam.phm.domain
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Comentario(val usuarioId: Long, val mensaje: String, val calificacion: Double, val fecha: LocalDate) :
    Entidad {
    @Id
    @GeneratedValue
    override var id = 0

}

