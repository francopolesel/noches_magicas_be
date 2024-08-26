package ar.edu.unsam.phm.domain

import java.time.LocalDate

data class ComentarioEntrada(
    val idUsuario: Long,
    val idEntrada: Long,
    val mensaje: String,
    val calificacion: Double,
    val fecha: LocalDate
)
