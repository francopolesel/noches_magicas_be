package ar.edu.unsam.phm.DTO

import ar.edu.unsam.phm.domain.EstadoDeShow
import ar.edu.unsam.phm.domain.Funcion
import ar.edu.unsam.phm.domain.Show
import ar.edu.unsam.phm.domain.Usuario
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.math.RoundingMode

@Document(collection = "shows")
data class ShowMongoDTO(
    val idBanda: Long,
    val nombreBanda: String,
    val evento: String,
    val estado :EstadoDeShow,
    val imagen: String,
    val idInstalacion: Long,
    val nombreInstalacion: String,
    val rate: Double,
    val cantComentarios: Int,
    val precioMin: BigDecimal,
    val precioMax: BigDecimal,
    val funciones: MutableSet<Funcion>,
    val costoDelShow: Double,
    val listaComentarios: List<Int>
){
    @Id
    lateinit var id: String
}

fun Show.toShowMongoDTO() = ShowMongoDTO(
    idBanda = banda.id,
    nombreBanda = banda.nombre,
    evento = evento,
    estado = estado,
    imagen = banda.imagen,
    idInstalacion = instalacion.id,
    nombreInstalacion = instalacion.nombre,
    rate = rate(),
    precioMax = entradaMasCara(),
    precioMin = entradaMasBarata(),
    cantComentarios = cantidadDeComentarios(),
    funciones = funciones,
    costoDelShow = costoDelShow,
    listaComentarios = listaComentarios.map{it.id}
)

