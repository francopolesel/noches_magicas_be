package ar.edu.unsam.phm.DTO

import ar.edu.unsam.phm.domain.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class DetalleFuncionDTO
    (
    val id :String,
    val fecha: LocalDateTime,
    val soldout: Boolean,
)

fun Funcion.toDTO() = DetalleFuncionDTO(
    id = this.id,
    fecha = this.fecha,
    soldout = this.soldOut()
)
