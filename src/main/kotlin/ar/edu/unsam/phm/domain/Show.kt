package ar.edu.unsam.phm.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.math.BigDecimal
import java.math.RoundingMode


class Show(
    val banda: Banda,
    val instalacion: Instalacion,
    var evento: String,
    val funciones: MutableSet<Funcion>,
) {

    var id: String = ""

    var estado: EstadoDeShow = EstadoDeShow.PRECIO_BASE

    var suscriptores: Int = 0

    var listaComentarios: MutableSet<Comentario> = mutableSetOf()

    var costoDelShow = costoDelShow()

    var rate = rate()

    var entradaMasBarata = entradaMasBarata()

    var entradaMasCara = entradaMasCara()

    var cantidadDeComentarios = cantidadDeComentarios()

    @JsonProperty
    //@Field("costoDelShow") Por alguna razon no se ve reflejado en el JSON
    fun costoDelShow() = banda.costo + instalacion.costoFijo()


    fun precioDeEntrada(tipoUbicacion: TipoUbicacion): Double =
        (costoDelShow() / instalacion.plazasTotales() + tipoUbicacion.costo) * estado.porcentaje

    fun obtenerPrecioEntradas(): List<Double> {
        val tipoUbicacionesEntradas = instalacion.capacidad.map { it.tipoUbicacion }
        return tipoUbicacionesEntradas.map { precioDeEntrada(it) }
    }

    fun entradaMasBarata(): BigDecimal = obtenerPrecioEntradas().min().toBigDecimal().setScale(2, RoundingMode.HALF_UP)

    fun entradaMasCara(): BigDecimal = obtenerPrecioEntradas().max().toBigDecimal().setScale(2, RoundingMode.HALF_UP)

    fun rentabilidad() = estado.porcentaje

    fun cambiarEstado(nuevoEstado: EstadoDeShow) {
        estado = nuevoEstado
    }

    @JsonProperty
    fun rate(): Double = if (tieneComentarios()) listaComentarios.sumOf { it.calificacion } / cantidadDeComentarios()
    else 0.0

    @JsonProperty
    fun cantidadDeComentarios() = listaComentarios.size

    fun tieneComentarios() = listaComentarios.size > 0

    fun recibirComentario(comentario: Comentario) {
        listaComentarios.add(comentario)
    }

    fun usuariosQueComentaron() = listaComentarios.map { it.usuarioId }

    fun usuarioComentoEnElShow(idUsuario: Long): Boolean = comentariosDelUsuario(idUsuario).isNotEmpty()

    fun comentarioDelUsuario(idUsuario: Long): Comentario =
        listaComentarios.first { it.usuarioId == idUsuario }

    fun comentariosDelUsuario(idUsuario: Long): List<Comentario> =
        listaComentarios.filter { it.usuarioId == idUsuario }

    fun eliminarComentario(comentario: Comentario) {
        listaComentarios.remove(comentario)
    }
    fun addSubscriptor() {
        suscriptores++
    }

}

class ShowBuilder{
    var banda: Banda = BandaBuilder().build()
    var instalacion: Instalacion= InstalacionBuilder().buildTeatro()
    var evento: String=""
    var funciones: MutableSet<Funcion> = mutableSetOf<Funcion>()

    fun banda(banda:Banda):ShowBuilder{
        this.banda=banda
        return this
    }
    fun instalacion(instalacion:Instalacion):ShowBuilder{
        this.instalacion=instalacion
        return this
    }
    fun evento(evento:String):ShowBuilder{
        this.evento=evento
        return this
    }
    fun agregarFuncion(funcion: Funcion):ShowBuilder{
        this.funciones.add(funcion)
        return this
    }
    fun build() = Show(banda,instalacion,evento,funciones)

}