package ar.edu.unsam.phm.DTO

import ar.edu.unsam.phm.domain.Entrada
import ar.edu.unsam.phm.domain.Show
import ar.edu.unsam.phm.domain.TipoUbicacion
import java.math.BigDecimal
import java.math.RoundingMode

data class EntradaAComprarDTO(
    val id:Long,
    val ubicacion: TipoUbicacion,
    val precio : BigDecimal
)
fun Entrada.toEntradaAComprarDTO(show: Show)= EntradaAComprarDTO(
    id=id,
    ubicacion = tipoUbicacion,
    precio= show.precioDeEntrada(this.tipoUbicacion).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
)
