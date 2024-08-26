package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.DTO.toShowMongoDTO
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AdministradorService(
    @Autowired val repoShows : ShowRepository,
    @Autowired val showService: ShowService,
    @Autowired val usuarioService:UsuarioService,
    @Autowired val funcionService: FuncionService,
    @Autowired val repoClickData: ClickRepository,
) {

    @Transactional
    fun obtenerEstadisticasShow(idShow: String, idAdmin: Long): EstadisticasShow {
        val show = obtenerShow(idShow)
        val usuarioAdministrador = usuarioService.getUsuarioAdministradorById(idAdmin)
        val funcionesDelShow = show.funciones

        return EstadisticasShow(
            ingresosTotalesShow(funcionesDelShow, show, usuarioAdministrador),
            entradasVendidasTotales(funcionesDelShow, usuarioAdministrador),
            show.costoDelShow(),
            entradasVendidasPorTipo(funcionesDelShow, usuarioAdministrador),
            funcionesDelShow.size,
            cantidadFuncionesSoldOut(funcionesDelShow),
            entradaMasBarata(show),
            show.suscriptores,
            show.estado,
            showPuedeCrearFuncion(show, usuarioAdministrador),
            show.evento ,
            obtenerCantidadDeClicksPorShow(idShow)
        )
    }

    fun showPuedeCrearFuncion(show: Show, administrador: UsuarioAdministrador): Boolean = administrador.puedoCrearFuncion(show.funciones, show.suscriptores, show.entradaMasBarata.toDouble(), show.costoDelShow())

    fun entradaMasBarata(show: Show) : BigDecimal = show.entradaMasBarata()
    fun entradasVendidasPorTipo(funcionesDelShow: MutableSet<Funcion>, administrador: UsuarioAdministrador): List<Pair<TipoUbicacion, Int>> =
        administrador.totalEntradasVendidasPorTipo(funcionesDelShow)

    fun entradasVendidasTotales(funcionesDelShow: MutableSet<Funcion>, administrador: UsuarioAdministrador): Int =
        administrador.totalEntradasVendidasShow(funcionesDelShow)

    fun cantidadFuncionesSoldOut(funcionesDelShow: MutableSet<Funcion>) = funcionService.cantidadFuncionesSoldout(funcionesDelShow)

    fun ingresosTotalesShow(funcionesShow: MutableSet<Funcion>, show: Show, administrador: UsuarioAdministrador): BigDecimal = administrador.ingresosTotalShow(funcionesShow,show).toBigDecimal().setScale(2, RoundingMode.HALF_UP)

    fun obtenerShow(idShow: String) = showService.showMongoDTOToShow(repoShows.findById(idShow).orElseThrow { Businessexception("El show con ID $idShow no fue encontrado") })

    fun obtenerFuncionesDeShow(show: Show): Set<Funcion> = show.funciones

    fun obtenerCantidadDeClicksPorShow(idShow: String): Int = repoClickData.contarEventosPorIdShowEspecifico(idShow)

    @Transactional
    fun actualizarEstadoShow(idShow: String, nuevoEstado: String, nuevoNombre: String, idAdmin: Long) {
        if (nuevoEstado.isBlank()) {
            throw Businessexception("El nuevo estado no puede estar vacío.")
        }

        val administrador = usuarioService.getUsuarioAdministradorById(idAdmin)
        val show = showService.findById(idShow)
        val nuevoEstadoEnum = obtenerEstadoFromString(nuevoEstado)

        administrador.cambiarEstadoDeUnShow(show, nuevoEstadoEnum)
        administrador.cambiarNombreDeUnShow(show, nuevoNombre)
        repoShows.save(show.toShowMongoDTO().apply { id = show.id })
    }

    fun obtenerEstadoFromString(estado: String): EstadoDeShow {
        val estadoShowMap: Map<String, EstadoDeShow> = mapOf(
            "PRECIO_BASE" to EstadoDeShow.PRECIO_BASE,
            "MEGA_SHOW" to EstadoDeShow.MEGA_SHOW,
            "VENTA_PLENA" to EstadoDeShow.VENTA_PLENA,
        )
        return estadoShowMap[estado] ?: throw IllegalArgumentException("Estado no valido: $estado")
    }

    fun obtenerEstadosShow(): List<EstadoDeShow> = EstadoDeShow.values().toList()
}