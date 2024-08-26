package ar.edu.unsam.phm.domain

enum class EstadoDeShow(val porcentaje: Double){
    PRECIO_BASE(0.8),
    VENTA_PLENA(1.0),
    MEGA_SHOW(1.3)
}