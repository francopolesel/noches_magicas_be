package ar.edu.unsam.phm.DTO

import java.time.LocalDateTime

data class ShowACrearDTO
    (
    val nombreBanda: String,
    val imagenBanda: String,
    val costoBanda: Double,
    val instalacionId: Long,
    val nombreShow: String,
)