package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.DTO.*
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.services.AdministradorService
import ar.edu.unsam.phm.services.ClickService
import ar.edu.unsam.phm.services.ShowService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"], methods = [RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS])
class ShowController(@Autowired val showService: ShowService, @Autowired val adminService: AdministradorService ,@Autowired val clickService:ClickService) {

    @GetMapping("/all-shows")
    @Operation(summary = "Devuelve la informacion de los shows")
    fun getShows(
        @RequestParam idUsuario: Long, @RequestParam artista: String?,
        @RequestParam locacion: String?, @RequestParam conAmigos: Boolean?
    ): List<CardShowMongoDTO> {
        val filtro = Filtro(artista ?: "", locacion ?: "", conAmigos ?: false)
        return showService.getShows(idUsuario, filtro)
    }

    @GetMapping("/test-shows")
    @Operation(summary = "Devuelve todos los shows")
    fun getTestShows(): List<Show> = showService.findAll()

    @GetMapping("/test-amigos")
    @Operation(summary = "Devuelve los amigos de un usuario que asisten al show")
    fun getTestAmigos(@RequestParam idUsuario: Long, @RequestParam idShow: String): Set<UsuarioDTO> =
        showService.getTestAmigos(idUsuario, idShow)

    @GetMapping("/shows")
    @Operation(summary = "Devuelve la informacion de los shows disponibles para su compra")
    fun getShowsDisponibles(
        @RequestParam idUsuario: Long, @RequestParam artista: String?,
        @RequestParam locacion: String?, @RequestParam conAmigos: Boolean?
    ): List<CardShowMongoDTO> {
        val filtro = Filtro(artista ?: "", locacion ?: "", conAmigos ?: false)
        return showService.getShowsDisponibles(idUsuario, filtro)
    }

    @GetMapping("/detalleShow")
    @Operation(summary = "Devuelve el detalle del show seleccionado")
    fun getShowById(@RequestParam idShow: String, @RequestParam idUsuario: Long):DetalleShowDTO {
        return showService.getShowById(idShow, idUsuario)
    }

    @PostMapping("/clicklog")
    @Operation(summary = "mantiene el registro de clicks")
        fun logClick(@RequestParam idShow: String, @RequestParam idUsuario: Long){
        clickService.registrarClick(idShow, idUsuario)  // para el registro de clicks
    }

    @GetMapping("/estadisticasShow/{id}")
    @Operation(summary = "Envia las estadisticas de un show")
    fun estadisticasShow(@PathVariable id: String, @RequestParam idAdmin: Long): EstadisticasShow =
        adminService.obtenerEstadisticasShow(id, idAdmin)

    @PostMapping("/editar-show/{idShow}")
    @Operation(summary = "Actualiza el estado de un show")
    fun actualizarEstadoShow(
        @PathVariable idShow: String,
        @RequestParam nuevoEstado: String,
        @RequestParam nuevoNombre: String,
        @RequestParam idAdmin: Long
    ) =
        adminService.actualizarEstadoShow(idShow, nuevoEstado, nuevoNombre, idAdmin)

    @GetMapping("/estados-show")
    @Operation(summary = "Envia los estados posibles para los shows")
    fun obtenerEstadosDisponiblesShow(): List<EstadoDeShow> = adminService.obtenerEstadosShow()

    @PostMapping("/crear-show")
    @Operation(summary = "Crea un show")
    fun registrarUsuario(@RequestBody showACrear: ShowACrearDTO) : ShowMongoDTO = showService.crearShow(showACrear)

}