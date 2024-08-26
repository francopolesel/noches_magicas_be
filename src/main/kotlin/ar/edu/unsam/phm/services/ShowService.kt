package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.DTO.*
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.repository.*
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.time.LocalDateTime


@Service
class ShowService(
    @Autowired val repoShows: ShowRepository,
    @Autowired val repoUsuario: UsuarioRepository,
    @Autowired val repoComentario: ComentarioRepository,
    @Autowired val repoBanda: BandaRepository,
    @Autowired val repoInstalacion: InstalacionRepository,
    @Autowired val comentarioService: ComentarioService,
    @Autowired var mongoTemplate: MongoTemplate
) {

    fun showsDisponibles(): Set<Show> {
        return repoShows.findShowsDisponibles().map{showMongoDTOToShow(it)}.toSet()
    }

    @Transactional
    fun getTestAmigos(idUsuario: Long, idShow: String): Set<UsuarioDTO> {
        val show =
            showMongoDTOToShow(repoShows.findById(idShow).orElseThrow { Businessexception("El show con ID $idShow no fue encontrado") })
        val usuario = repoUsuario.findById(idUsuario)
            .orElseThrow { Businessexception("El usuario con ID $idUsuario no fue encontrado") }
        return usuario.amigosQueAsisten(show).map { it.toDTO() }.toSet()
    }

    fun findAll(): List<Show> = repoShows.findAll().toList().map { showMongoDTOToShow(it) }

    fun findById(idShow: String): Show {
        return showMongoDTOToShow(repoShows.findById(idShow).orElseThrow {
            Businessexception("El show con ID $idShow no fue encontrado")
        }).apply{  id=idShow  }
    }

    fun obtenerDetalleFunciones(show: Show): List<DetalleFuncionDTO> {
        val funcionesAsociadas = show.funciones
        return funcionesAsociadas.map { it.toDTO() }
    }

    @Transactional
    fun getShowsDisponibles(idUsuario: Long, filtro: Filtro): List<CardShowMongoDTO> {

        val showsDisponibles = repoShows.findShowsDisponiblesByBandaNombreContainingIgnoreCaseAndInstalacionNombreContainingAndFuncionesFechaGreaterThan(
            filtro.artista,
            filtro.locacion,
            LocalDateTime.now()
        ).map {
            it.toCardShowDTO(getAmigosQueAsisten(it,idUsuario))
        }

        return if (filtro.conAmigos) {
            showsDisponibles.filter { it ->
                it.amigosQueAsisten.isNotEmpty()
            }
        } else {
            showsDisponibles
        }
    }

    @Transactional //PREGUNTAR A NICO
    fun getShows(idUsuario: Long, filtro: Filtro): List<CardShowMongoDTO> {
        return repoShows.findByBandaNombreContainingIgnoreCaseAndInstalacionNombreContainingIgnoreCase(
            filtro.artista,
            filtro.locacion
        ).map {
            it.toCardShowDTO(getAmigosQueAsisten(it,idUsuario))
        }.filter { filtro.conAmigos == it.amigosQueAsisten.isNotEmpty() }
    }

    @Transactional
    fun getShowById(idShow: String, idUsuario: Long): DetalleShowDTO {
        val show = findById(idShow)
        val comentarios = show.listaComentarios
        return show.toDTO(
            findByIdUsuario(idUsuario),
            obtenerDetalleFunciones(show),
            comentarios.map {
                it.toDTO(
                    repoUsuario.findById(it.usuarioId)
                        .orElseThrow { Businessexception("El Usuario con ID $idUsuario no fue encontrado") })
            })
    }

    fun getComentarios(idUsuario: Long): List<ShowConComentarioDTO> {
        val usuario = repoUsuario.findById(idUsuario)
            .orElseThrow { Businessexception("El Usuario con ID $idUsuario no fue encontrado") }
        val comentarios = repoComentario.getComentarioByUsuarioId(idUsuario)
        return repoShows.findShowMongoDTOSByListaComentariosIsContainingComentariosId(comentarios.map { it.id }).map{showMongoDTOToShow(it)}.map { it.toDTOComentario(usuario) }
    }

    //VERIFICAR, se implementó por el momento con MongoTemplate
    @Transactional
    fun deleteComentario(idComentario: Int) {
        val show = getByIdComentario(idComentario)
        val comentario = comentarioService.findById(idComentario)

        show.listaComentarios.remove(comentario)
        repoShows.save(show.toShowMongoDTO().apply { id = show.id })

    }

    fun getByIdComentario(idComentario: Int): Show {
        return showMongoDTOToShow(repoShows.findByComentarioNumero(idComentario)
            ?: throw Businessexception("no existe ese show para ese comentario"))
    }

    fun getShowDeFuncion(funcion: Funcion): Show {
        return showMongoDTOToShow(repoShows.findByFuncionesId(funcion.id)
            ?: throw Businessexception("esa funcion no pertenece a ningun show"))
    }

    fun findByIdUsuario(idUsuario: Long): Usuario {
        return repoUsuario.findById(idUsuario).orElseThrow {
            Businessexception("El usuario con ID $idUsuario no fue encontrado")
        }
    }

    fun showMongoDTOToShow(showMongoDTO: ShowMongoDTO): Show{
        val banda = repoBanda.findById(showMongoDTO.idBanda).orElseThrow{ Businessexception("No se encontró la banda")}
        val instalacion = repoInstalacion.findById(showMongoDTO.idInstalacion).orElseThrow{ Businessexception("No se encontró la instalación")}
        val listaComentariosCompleta = showMongoDTO.listaComentarios.map { repoComentario.findById(it).orElseThrow{ Businessexception("No se encontró el comentario")} }.toMutableSet()

        return Show(banda, instalacion, showMongoDTO.evento, showMongoDTO.funciones).apply{ id=showMongoDTO.id; listaComentarios = listaComentariosCompleta; estado=showMongoDTO.estado }
    }

    fun getAmigosQueAsisten(showMongo: ShowMongoDTO, idUsuario: Long): List<UsuarioDTO> {
        val usuario = repoUsuario.findById(idUsuario).orElseThrow { Businessexception("No se encontro el usuario") }
        return usuario.amigosQueAsisten(showMongoDTOToShow(showMongo)).map { it.toDTO()}
    }

    fun crearShow(showACrear: ShowACrearDTO): ShowMongoDTO {
        val bandaNueva = BandaBuilder().nombre(showACrear.nombreBanda).imagen(showACrear.imagenBanda).costo(showACrear.costoBanda).build()
        val instalacion = repoInstalacion.findById(showACrear.instalacionId).orElseThrow { Businessexception("No se encontro la instalacion") }
        val showNuevo = ShowBuilder().banda(bandaNueva).instalacion(instalacion).evento(showACrear.nombreShow).build()
        repoBanda.save(bandaNueva)

        return repoShows.save(showNuevo.toShowMongoDTO())
    }


}