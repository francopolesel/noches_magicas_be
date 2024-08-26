package ar.edu.unsam.phm.DTO

import ar.edu.unsam.phm.domain.Comentario
import ar.edu.unsam.phm.domain.Show
import ar.edu.unsam.phm.domain.Usuario
import java.time.LocalDate

data class ComentarioDTO
    (
        val id: Int,
        val nombre: String,
        val fecha: LocalDate,
        val mensaje: String,
        val calificacion: Double,
        val imagen: String,
    )

fun Comentario.toDTO(usuario: Usuario) = ComentarioDTO(
    id = id,
    nombre = "${usuario.nombre} ${usuario.apellido}",
    fecha = fecha,
    mensaje = mensaje,
    calificacion = calificacion,
    imagen = usuario.imagen,
)

data class ShowConComentarioDTO
    (
        val nombre: String,
        val evento: String,
        val imagen: String,
        val comentario: ComentarioDTO,
    )

fun Show.toDTOComentario(usuario: Usuario) = ShowConComentarioDTO(
    nombre = banda.nombre,
    evento = evento,
    imagen = banda.imagen,
    comentario = comentarioDelUsuario(usuario.id).toDTO(usuario)
)

