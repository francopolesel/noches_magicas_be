package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.DTO.*
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.neo4j.EntradaNeo4jRepository

import ar.edu.unsam.phm.repository.ComentarioRepository
import ar.edu.unsam.phm.repository.ShowRepository
import ar.edu.unsam.phm.repository.UsuarioRepository
import ar.edu.unsam.phm.neo4j.UsuarioNeo4jRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    @Autowired val repositorioUsuarios: UsuarioRepository,
    @Autowired val repositorioUsuariosNeo4j: UsuarioNeo4jRepository,
    @Autowired val entradaService: EntradasService,
    @Autowired val repositorioComentarios: ComentarioRepository,
    @Autowired val repositorioShow: ShowRepository,
    @Autowired val showService: ShowService,
    @Autowired val carritoService: CarritoService,
    @Autowired val funcionService: FuncionService,
    @Autowired val repositorioEntradaNeo4j: EntradaNeo4jRepository,
) {

    fun findById(id: Long): Usuario = repositorioUsuarios.findById(id).orElseThrow { Businessexception("El Usuario con ID $id no fue encontrado") }

    fun usuarios() = repositorioUsuarios.findAll().toList()


    fun getUsuarioComunById(id: Long): UsuarioComun = repositorioUsuarios.getUsuarioComunById(id)

    @org.springframework.transaction.annotation.Transactional
    fun getAmigosSugeridos(id: Long): MutableSet<UsuarioDTO> {
        return repositorioUsuariosNeo4j.findAmigosSugeridos(id).toMutableSet().map{it.toDTO()}.toMutableSet()
    }

    fun getUsuarioAdministradorById(id: Long): UsuarioAdministrador = repositorioUsuarios.getUsuarioAdminById(id)

    fun validarExistenciaByUsername(username: String) {
        val elementoEncontrado = repositorioUsuarios.findByUsername(username)
        if (elementoEncontrado != null) {
            throw ElementoYaExisteException("Usuario existente.")
        }
    }

    fun validarSaldoInsuficiente(entrada: Entrada, usuario: Usuario): Boolean {
        val show = showService.findById(entrada.idShow)
        return show.precioDeEntrada(entrada.tipoUbicacion) > usuario.saldo
    }

//    @Transactional
//    fun getTotalCarrito(idUsuario: Int): Double {
//        return repositorioCarrito.getCarritoByIdUsuario(idUsuario).entradas.sumOf {
//            showService.findById(it.idShow).precioDeEntrada(it.tipoUbicacion)
//        }
//    }


    @Transactional
    fun actualizarUsuario(id: Long, usuarioActualizado: UsuarioPerfilDTO) {
        if (usuarioActualizado.nombre.isEmpty() ||
            usuarioActualizado.apellido.isEmpty()
        ) {
            throw Businessexception("El usuario a actualizar no puede tener campos vacíos.")
        }

        val usuarioActual = findById(id)

        usuarioActual.apply {
            nombre = usuarioActualizado.nombre
            apellido = usuarioActualizado.apellido
            fechaNacimiento = usuarioActualizado.fechaDeNacimiento
        }
        repositorioUsuarios.save(usuarioActual)
    }

    @Transactional
    fun agregarCredito(id: Long, credito: Double) {
        if (credito > 0) {
            findById(id).agregarSaldo(credito)
        } else throw Businessexception("El credito a agregar tiene que ser mayor que 0")
    }

    fun getAmigos(idUsuario: Long) = findById(idUsuario).amigos.map { it.toDTO() }

    fun getAmigosCompletos(idUsuario: Long) = findById(idUsuario).amigos

    @Transactional
    fun deleteAmigo(idUsuario: Long, idAmigo: Long) {
        val amigo = findById(idAmigo)
        val usuario = findById(idUsuario)
        if (usuario.esAmigo(amigo)) {
            usuario.eliminarAmigo(amigo)
            repositorioUsuariosNeo4j.eliminarRelacionAmigo(idUsuario, idAmigo)
        } else throw Businessexception("El usuario no puede eliminar a ese amigo porque no esta en su lista de amigos")
    }

    @Transactional

    fun comprarEntradas(idUsuario: Long) {

        val usuarioComprador = getUsuarioComunById(idUsuario)
        val carrito = carritoService.findById(idUsuario.toString())
        carrito.entradas.forEach {
            val show = showService.findById(it.idShow)
            validarSaldoInsuficiente(it, usuarioComprador)
            val funcion = funcionService.findById(it.idFuncion)
                val entrada = repositorioEntradaNeo4j.save(it)
            println(entrada.id)
            repositorioUsuariosNeo4j.comprarEntrada(usuarioComprador.id, entrada.id)
            funcion.venderEntrada(
                it,
                usuarioComprador,
                show.precioDeEntrada(it.tipoUbicacion)
            )
        }


        carritoService.clearCarrito(idUsuario.toString())
    }


    @Transactional
    fun getEntradasCompradas(id: Long): List<EntradaConShowDTO> {
        val usuario = findById(id)
        return usuario.entradas.map {
            it.toDTO(
                usuario,
                showService.findById(it.idShow),
                funcionService.findById(it.idFuncion)
            )
        }
    }


    fun getLowDataById(id: Long): UsuarioDTO {
        return findById(id).toDTO()
    }


    @Transactional
    fun comentarShow(comentario: ComentarioEntrada) {
        val usuario = getUsuarioComunById(comentario.idUsuario)
        val comentarioShow = Comentario(usuario.id, comentario.mensaje, comentario.calificacion, comentario.fecha)
        val entrada = entradaService.findById(comentario.idEntrada)
        val show = showService.findById(entrada.idShow)
        if (show.usuarioComentoEnElShow(usuario.id)) {
            throw Businessexception("El usuario ya comento en el show")
        } else if ((comentario.calificacion < 0.0 || comentario.calificacion > 5.0)) {
            throw Businessexception("la calificacion no es valida")
        } else {
            repositorioComentarios.save(comentarioShow)
            show.recibirComentario(comentarioShow)
            repositorioShow.save(show.toShowMongoDTO().apply { id = show.id })
        }
    }

    fun findFuncionById(id: String): Funcion {
        return repositorioShow.findFuncionByIdInShows(id) ?: throw Businessexception("No se encontro al show")
    }

    @Transactional
    @org.springframework.transaction.annotation.Transactional
    fun agregarAmigos(idUsuario: Long, idAmigo: Long) {
        println(idUsuario)
        println(idAmigo)
        val usuario =repositorioUsuarios.getUsuarioComunById(idUsuario)
        val amigo= repositorioUsuarios.getUsuarioComunById(idAmigo)
        usuario.agregarAmigo(amigo)
        repositorioUsuariosNeo4j.save(usuario)
    }

}

