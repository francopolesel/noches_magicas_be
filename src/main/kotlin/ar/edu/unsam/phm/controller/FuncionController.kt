package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.domain.EntradasAComprar
import ar.edu.unsam.phm.domain.Funcion
import ar.edu.unsam.phm.domain.TipoUbicacion
import ar.edu.unsam.phm.services.FuncionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"], methods = [RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS])
class FuncionController(
    @Autowired val funcionService: FuncionService

) {
    @GetMapping("/funcion/entradas/{id}")
    @Operation(summary = "devuelve las entradas del show")
    fun getEntradas(@PathVariable id: String): List<EntradasAComprar> {
        return funcionService.getEntradas(id)
    }

    @GetMapping("/funcionDeShow")
    fun findById(@RequestParam idFuncion: String) = funcionService.findById(idFuncion)

    @GetMapping("/entradasDisponibles")
    @Operation(summary = "devuelve las entradas por ubicacion")
    fun getEntradasDisponibles(@RequestParam tipoUbicacion: TipoUbicacion, @RequestParam idFuncion: String): Int {
        return funcionService.getEntradasDisponibles(tipoUbicacion, idFuncion)
    }

    @PostMapping("/funcion/suscribirse-a-show/{id}")
    @Operation(summary = "suscribe a un usuario a un show")
    fun getLowDataById(@PathVariable id: Long, @RequestParam showId: String) {
        funcionService.suscribirseAUnShow(showId , id)
    }




    @PostMapping("/crear-funcion/{idShow}")
    @Operation(summary = "crea nueva funcion")
    fun crearFuncion(@PathVariable idShow: String, @RequestParam fecha: String): Funcion {
        return funcionService.crearFuncion(idShow, fecha)
    }

}
