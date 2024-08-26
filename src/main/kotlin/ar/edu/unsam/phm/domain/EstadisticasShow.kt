package ar.edu.unsam.phm.domain

import ar.edu.unsam.phm.domain.TipoUbicacion
import java.math.BigDecimal

data class EstadisticasShow(
    val ingresosTotalesShow: BigDecimal,
    val entradasVendidasTotales: Int,
    val costoTotal: Double,
    val entradasVendidasPorTipo: List<Pair<TipoUbicacion, Int>>,
    val cantidadFuncionesShow: Int,
    val cantidadFuncionesSoldOut: Int,
    val entradaMasBarata: BigDecimal,
    val cantidadSuscriptores: Int,
    val estadoDeShow: EstadoDeShow,
    val puedoCrearFuncion: Boolean,
    val nombreShow: String,
    val cantidadDeClicks: Int
) {

}

