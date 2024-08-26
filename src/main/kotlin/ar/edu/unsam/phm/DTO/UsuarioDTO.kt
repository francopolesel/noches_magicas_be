package ar.edu.unsam.phm.DTO

import ar.edu.unsam.phm.domain.Usuario
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class UsuarioDTO(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val imagen: String,
)

fun Usuario.toDTO() = UsuarioDTO(
    id = id,
    nombre = nombre,
    apellido = apellido,
    imagen = imagen
)

class UsuarioPerfilDTO(
    val nombre: String,
    val apellido: String,
    val imagen: String,
    val fechaDeNacimiento: LocalDate,
    val credito: BigDecimal,
    val esAdmin: Boolean
)

fun Usuario.toUsuarioPerfilDTO() = UsuarioPerfilDTO(
    nombre = nombre,
    apellido = apellido,
    imagen = imagen,
    fechaDeNacimiento = fechaNacimiento,
    credito = saldo.toBigDecimal().setScale(2, RoundingMode.HALF_UP),
    esAdmin = esAdministrador()
)