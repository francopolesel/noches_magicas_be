package ar.edu.unsam.phm.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class ClickData(
    val fecha: LocalDateTime, val idShow: String,
    val nombreDelShow: String, val nombreDelUsuario: String,
    val idDelUsuario: Long
) {
    @Id
    lateinit var id: String
}