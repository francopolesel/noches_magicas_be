package ar.edu.unsam.phm.DTO

import ar.edu.unsam.phm.domain.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime


//Detalle
data class DetalleShowDTO
    (
    val id: String,
    val banda: String,
    val evento: String,
    val imagen: String,
    val instalacion: Instalacion,
    val rate: Double,
    val cantComentarios: Int,
    val precioMin: BigDecimal,
    val precioMax: BigDecimal,
    val detalleFunciones: List<DetalleFuncionDTO>,
    var listaComentarios: List<ComentarioDTO>,
    val amigosQueAsisten: List<UsuarioDTO>,
){}

fun Show.toDTO(usuario: Usuario, detalleFunciones: List<DetalleFuncionDTO>, comentariosDTO: List<ComentarioDTO>) = DetalleShowDTO(
    id = id,
    banda = banda.nombre,
    evento = evento,
    imagen = banda.imagen,
    rate = rate(),
    precioMax = entradaMasCara,
    precioMin = entradaMasBarata,
    cantComentarios = cantidadDeComentarios(),
    instalacion = instalacion,
    detalleFunciones = detalleFunciones,
    listaComentarios = comentariosDTO,
    amigosQueAsisten = usuario.amigosQueAsisten(this).map { it.toDTO() }
)

//Shows
data class CardShowMongoDTO
    (
    val id: String,
    val banda: String,
    val evento: String,
    val imagen: String,
    val nombreInstalacion: String,
    val rate: Double,
    val cantComentarios: Int,
    val precioMin: BigDecimal,
    val precioMax: BigDecimal,
    val detalleFunciones: MutableSet<LocalDateTime>,
    val amigosQueAsisten: List<UsuarioDTO>,
){}

fun ShowMongoDTO.toCardShowDTO(amigosQueAsisten: List<UsuarioDTO>) = CardShowMongoDTO(
    id = id,
    banda = nombreBanda,
    evento = evento,
    imagen = imagen,
    rate = rate,
    precioMax = precioMax,
    precioMin = precioMin,
    cantComentarios = cantComentarios,
    nombreInstalacion = nombreInstalacion,
    detalleFunciones = funciones.map{it.fecha}.toMutableSet(),
    amigosQueAsisten = amigosQueAsisten
)

data class EntradaConShowDTO(
    val id: Long,
    val precio: Double,
    val banda: String,
    val evento: String,
    val imagen: String,
    val instalacion: Instalacion,
    val rate: Double,
    val cantComentarios: Int,
    val detalleFunciones: List<DetalleFuncionDTO>,
    val amigosQueAsisten: List<UsuarioDTO>,
    val puedeCalificar: Boolean,
    val idDelShow:String,
    val tipoUbicacion: String
)

fun Entrada.toDTO(usuario: Usuario, show: Show, funcion: Funcion):EntradaConShowDTO {

    return EntradaConShowDTO(
        id = id,
        precio = show.precioDeEntrada(this.tipoUbicacion),
        banda = show.banda.nombre,
        evento = show.evento,
        imagen = show.banda.imagen,
        rate = show.rate(),
        cantComentarios = show.cantidadDeComentarios(),
        instalacion = show.instalacion,
        detalleFunciones = listOf(funcion.toDTO()),
        amigosQueAsisten = usuario.amigosQueAsisten(show).map { it.toDTO() },
        puedeCalificar = !show.usuarioComentoEnElShow(usuario.id) && funcion.fueRealizado(),
        idDelShow = idShow,
        tipoUbicacion = tipoUbicacion.name
    )
}