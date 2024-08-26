package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.DTO.toShowMongoDTO
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.repository.ShowRepository
import ar.edu.unsam.phm.repository.UsuarioRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
class FuncionService(
    @Autowired val showService: ShowService,
    @Autowired val repoShows: ShowRepository,
    @Autowired val repoUsuarios: UsuarioRepository
) {


    fun findById(funcionId: String): Funcion {
        return findByFuncionesId(funcionId).funciones.find { it.id ==  funcionId}?:throw Businessexception("No se encontro el show")
    }

    fun findByFuncionesId(funcionId: String): Show {
        return showService.showMongoDTOToShow(repoShows.findByFuncionesId(funcionId)?:throw Businessexception("No se encontro el show"))
    }

    fun getFuncionesDelShow(show: Show): Set<Funcion> {
        return showService.findById(show.id).funciones
    }

    @Transactional
    fun getEntradasDisponibles(tipoUbicacion: TipoUbicacion, idFuncion: String): Int {
        val funcion = findById(idFuncion)
        return funcion.entradasDisponiblesPorUbicacion(tipoUbicacion)
    }

    fun cantidadFuncionesSoldout(funciones: Set<Funcion>): Int = funciones.count { it.soldOut() }

    fun getFuncionesDelShow(showId: String): Set<Funcion> {
        val show = showService.findById(showId)
        return show.funciones
    }

    fun fechasFunciones(showId: String): List<LocalDateTime> = getFuncionesDelShow(showId).map {
        it.fecha
    }

    fun todasLasFuncionesSoldOut(showID: String) = getFuncionesDelShow(showID).all { it.soldOut() }

    @Transactional
    fun suscribirseAUnShow(showID: String, usuarioId: Long) {
        val usuario = repoUsuarios.getUsuarioComunById(usuarioId)
        val show = showService.findById(showID)
        if (getFuncionesDelShow(showID).all { it.soldOut() }) {
            show.addSubscriptor()
        } else
            throw Businessexception("No estan todos los shows Sold Out")
    }


    @Transactional
    fun getEntradas(id: String): List<EntradasAComprar> {
        val funcion = findById(id)
        val ubicacion = funcion.ubicaciones()
        val show = showService.getShowDeFuncion(funcion)
        return ubicacion.map {
            EntradasAComprar(
                id,
                it,
                show.precioDeEntrada(it).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
            )
        }
    }


    @Transactional
    fun crearFuncion(idShow: String, fecha: String): Funcion {
        if (fecha.isBlank()) {
            throw Businessexception("La fecha no puede estar vacía.")
        }
        val show = showService.findById(idShow)
        val funcionNueva = FuncionBuilder().fecha(LocalDateTime.parse(fecha)).tipoDeEntrada(show.instalacion.capacidad.toSet()).build()
//        val fechaNueva = LocalDateTime.parse(fecha)
//        val nuevaFuncion = Funcion(fechaNueva, show.instalacion.capacidad.toSet())
        show.funciones.add(funcionNueva)
        repoShows.save(show.toShowMongoDTO().apply { id = show.id })
        return funcionNueva
    }


}