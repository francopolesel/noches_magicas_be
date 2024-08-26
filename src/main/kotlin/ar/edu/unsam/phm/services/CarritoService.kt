package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.DTO.EntradaConShowDTO
import ar.edu.unsam.phm.DTO.toDTO
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.repository.CarritoRepository
import ar.edu.unsam.phm.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class CarritoService(
    @Autowired val repositorioCarrito: CarritoRepository,
    @Autowired val funcionService: FuncionService,
    @Autowired val showService: ShowService,
    @Autowired val repoUsuario: UsuarioRepository
) {

    fun crearCarrito(idUsuario: Long): CarritoDeCompras =
        repositorioCarrito.save(CarritoDeCompras(idUsuario))

    fun findById(id: String) =
        repositorioCarrito.findById(id).orElseThrow { Businessexception("el usuario no tiene un carrito asignado") }

    fun comprobarCarrito(id: String): CarritoDeCompras {
        return if(repositorioCarrito.existsById(id)){
            findById(id)
        } else {
            crearCarrito(id.toLong())
        }
    }

    @Transactional
    fun agregarEntradasAlCarrito(idUsuario: Long, listaEntradaCarrito: MutableList<SolicitudEntradaCarrito>) {
        val carrito = comprobarCarrito(idUsuario.toString())

        listaEntradaCarrito.forEach { solicitud ->
            val func = funcionService.findById(solicitud.id)
            val show = showService.getShowDeFuncion(func)
            if (funcionService.getEntradasDisponibles(solicitud.ubicacion, solicitud.id) >= solicitud.cantidad) {
                repeat(solicitud.cantidad) {
                    val numeroEntrada = Random.nextInt(1, 100001)
                    val entrada = Entrada(numeroEntrada, solicitud.ubicacion, func.id, show.id)
                    carrito.apply { entradas.add(entrada) }
                }
                repositorioCarrito.save(carrito)
            } else {
                throw Businessexception("No hay suficientes entradas para las cantidades que usted pidio")
            }
        }
    }

    @Transactional(readOnly = true)
    fun getCarrito(id: String): List<EntradaConShowDTO> {
        val carrito = findById(id)
        val usuario = repoUsuario.findById(id.toLong()).orElseThrow { Businessexception("el usuario no existe") }
        return carrito.entradas.map {
            it.toDTO(
                usuario,
                showService.findById(it.idShow),
                funcionService.findById(it.idFuncion)
            )
        }
    }

    @Transactional()
    fun getTotalCarrito(idUsuario: Int): Double {
        return comprobarCarrito(idUsuario.toString()).entradas.sumOf {
            showService.findById(it.idShow).precioDeEntrada(it.tipoUbicacion)
        }
    }

    @Transactional
    fun clearCarrito(id: String) {
        val carrito = findById(id)
        repositorioCarrito.save(carrito.apply { entradas = mutableListOf() })
    }

    @Transactional
    fun deleteEntradaCarrito(idUsuario: Long, idShow: String, ubicacion: String) {
        val carrito = findById(idUsuario.toString())
        val entradaABorrar = carrito.entradas.find {
            it.idShow == idShow && it.tipoUbicacion  == obtenerTipoUbicacionFromString(ubicacion)}
        if (entradaABorrar != null) {
            carrito.entradas.remove(entradaABorrar)
            repositorioCarrito.save(carrito)
        } else {
            throw NoSuchElementException("No se encontró la entrada en el carrito del usuario $idUsuario")
        }
    }

    fun obtenerTipoUbicacionFromString(ubicacion: String): TipoUbicacion {
        val ubicacionMap: Map<String, TipoUbicacion> = mapOf(
            "PLATEA_ALTA" to TipoUbicacion.PLATEA_ALTA,
            "CAMPO" to TipoUbicacion.CAMPO,
            "PALCO" to TipoUbicacion.PALCO,
            "PULLMAN" to TipoUbicacion.PULLMAN,
            "PLATEA_BAJA" to TipoUbicacion.PLATEA_BAJA
        )
        return ubicacionMap[ubicacion] ?: throw IllegalArgumentException("Ubicacion no valida: $ubicacion")
    }
}