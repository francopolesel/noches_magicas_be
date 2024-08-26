package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.DTO.*
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.services.CarritoService
import ar.edu.unsam.phm.services.ShowService
import ar.edu.unsam.phm.services.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(
    origins = ["*"],
    methods = [RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS]
)
class UsuarioController(
    @Autowired val usuarioService: UsuarioService,
    @Autowired val showService: ShowService,
    @Autowired val carritoService:CarritoService
) {

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "devuelve un usuario por su id")
    fun getUsuarioById(@PathVariable id: Long) = usuarioService.findById(id)

    @GetMapping("/perfil-usuario/{id}")
    @Operation(summary = "devuelve los datos del perfil de un usuario normal por su id")
    fun getPerfilUsuarioById(@PathVariable id: Long) = usuarioService.findById(id).toUsuarioPerfilDTO()

    @PutMapping("/actualizar-usuario/{id}")
    @Operation(summary = "recibe los datos nuevos del usuario y los actualiza")
    fun actualizarUsuario(@RequestBody request: UsuarioPerfilDTO, @PathVariable id: Long) =
        usuarioService.actualizarUsuario(id, request)

    //ESTE ENDPOINT ES SOLO PARA PRUEBAS
    @GetMapping("/usuarios")
    @Operation(summary = "Devuelve la informacion de los usuarios")
    fun getUsuarios() = usuarioService.usuarios()

    @PutMapping("/sumar-credito/{id}")
    @Operation(summary = "recibe al usuario y le agrega la cantidad ingresada en credito")
    fun sumarSaldo(@RequestBody request: Map<String, Double>, @PathVariable id: Long) {
        val cantidad = request["cantidad"]
        if (cantidad != null) usuarioService.agregarCredito (id, cantidad) else
            throw Businessexception("por favor ingrese una cantidad de dinero valida para acreditar")
    }

    @GetMapping("/perfil-usuario/amigos")
    @Operation(summary = "Devuelve los usuarios que son amigos del usuario ogueado")
    fun getAmigos(@RequestParam idUsuario: Long): List<UsuarioDTO> = usuarioService.getAmigos(idUsuario)

    @DeleteMapping("/deleteAmigo")
    @Operation(summary = "Elimina un usuario de la lista de amigos del usuario logueado")
    fun deleteAmigo(@RequestParam idUsuario: Long, idAmigo: Long) = usuarioService.deleteAmigo(idUsuario, idAmigo)

    @GetMapping("/perfil-usuario/entradas")
    @Operation(summary = "Devuelve las entradas del usuario logueado")
    fun getEntradasCompradas(@RequestParam idUsuario: Long): List<EntradaConShowDTO> =
        usuarioService.getEntradasCompradas(idUsuario)

    @PutMapping("/agregar-entradas-carrito/{id}")
    @Operation(summary = "agregar las entradas al carrito de compras")

    fun comprarEntradas(@PathVariable id: Long, @RequestBody entradasAComprar: MutableList<SolicitudEntradaCarrito>) {
        carritoService.agregarEntradasAlCarrito(id, entradasAComprar)

    }

    @GetMapping("/usuario-low-data/{id}")
    @Operation(summary = "devuelve solo imagen, nombre y apellido del usuario")
    fun getLowDataById(@PathVariable id: Long): UsuarioDTO = usuarioService.getLowDataById(id)

    @GetMapping("/sugerencia-amigos")
    @Operation(summary = "Devuelve una lista de amigos sugeridos")
    fun getAmigosSugeridos(@RequestParam idUsuario: Long): MutableSet<UsuarioDTO> = usuarioService.getAmigosSugeridos(idUsuario)

    @PutMapping("/agregar-amigo")
    @Operation(summary="Agrega amigos a un usuario")
    fun agregarAmigos(@RequestParam idUsuario: Long,idAmigo: Long ){
        usuarioService.agregarAmigos(idUsuario,idAmigo)
    }

    @GetMapping("/cart")
    @Operation(summary = "Devuelve la informacion de los shows en el carrito de compras")
    fun getCarrito(@RequestParam idUsuario: Int): List<EntradaConShowDTO> = carritoService.getCarrito(idUsuario.toString())

    @GetMapping("/totalCarrito")
    @Operation(summary = "Devuelve el total de precio del carrito de compras")
    fun getTotalCarrito(@RequestParam idUsuario: Int): Double = carritoService.getTotalCarrito(idUsuario)

    @PostMapping("/clearCart")
    @Operation(summary = "Limpia el carrito")
    fun clearCarrito(@RequestBody idUsuario: Int) = carritoService.clearCarrito(idUsuario.toString())


    @PostMapping("/comprar-entradas")
    @Operation(summary = "Compra todas las entradas que estan en el carrito")
    fun comprarEntradas(@RequestBody idUsuario: Long) = usuarioService.comprarEntradas(idUsuario)

    @PostMapping("/comentarShow")
    @Operation(summary = "Permite comentar un show, con un rate")
    fun comentarShow(@RequestBody comentario: ComentarioEntrada) = usuarioService.comentarShow(comentario)

    @GetMapping("/perfil/comentarios")
    @Operation(summary = "Devuelve los comentarios de un usuario en particular")
    fun getComentarios(@RequestParam idUsuario: Long): List<ShowConComentarioDTO> = showService.getComentarios(idUsuario)

    @DeleteMapping("/deleteComentario")
    @Operation(summary = "Elimina un comentario de los shows al que fue el usuario")
    fun deleteComentario(@RequestParam idComentario: Int) = showService.deleteComentario(idComentario)

    @DeleteMapping("/deleteEntradaCarrito")
    @Operation(summary = "Elimina una entrada del carrito del usuario")
    fun deleteEntradaCarrito(@RequestParam idUsuario: Long, idShow: String, tipoUbicacion: String) = carritoService.deleteEntradaCarrito(idUsuario, idShow, tipoUbicacion)

}
