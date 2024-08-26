package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.DTO.ShowMongoDTO
import ar.edu.unsam.phm.domain.Comentario
import ar.edu.unsam.phm.domain.Funcion
import ar.edu.unsam.phm.domain.Show
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime
import java.util.*

interface ShowRepository : MongoRepository<ShowMongoDTO, String> {

    @Query("{ 'funciones.finalizo': false }")
    fun findShowsDisponibles(): Set<ShowMongoDTO>

    fun findByFuncionesId(idFuncion: String): ShowMongoDTO?

    //@Query("{'listaComentarios.idComentario': ?0 }")
    //fun findByListaComentariosId(idComentario: Int): ShowMongoDTO?
    @Query("{ 'listaComentarios': ?0 }")
    fun findByComentarioNumero(comentarioNumero: Int): ShowMongoDTO

    //VERIFICAR, se implementó por el momento con MongoTemplate
//    @Query("{'_id': ?0}", delete = true)
//    fun deleteComment(showId: String, comentarioId: Int){
//    }
    @Query("{ 'listaComentarios': { \$in: ?0 } }")
    fun findShowMongoDTOSByListaComentariosIsContainingComentariosId(comentariosId: List<Int>): List<ShowMongoDTO>

    @Query("{'nombreBanda': {\$regex: ?0, \$options: 'i'}, 'nombreInstalacion': {\$regex: ?1, \$options: 'i'}}")
    fun findByBandaNombreContainingIgnoreCaseAndInstalacionNombreContainingIgnoreCase(
        artista: String,
        locacion: String
    ): List<ShowMongoDTO>

    @Query("{'nombreBanda': {\$regex: ?0, \$options: 'i'}, 'nombreInstalacion': {\$regex: ?1, \$options: 'i'}, 'funciones.fecha': {\$gt: ?2}}")
    fun findShowsDisponiblesByBandaNombreContainingIgnoreCaseAndInstalacionNombreContainingAndFuncionesFechaGreaterThan(
        artista: String,
        locacion: String,
        fechaActual: LocalDateTime
    ): Set<ShowMongoDTO>

    @Query(value = "{ 'funciones._id': ?0 }", fields = "{ 'funciones.$': 1 }")
    fun findFuncionByIdInShows(funcionId: String): Funcion?

    @Query("{ '_id' : ?0, 'funciones._id': ?1 }")
    fun findFunctionByIdInShow(showId: String, functionId: String): Funcion?
    fun findFuncionById(idFuncion: String): Funcion? {
        val showWithFunction = findByFuncionesId(idFuncion)
        return showWithFunction?.funciones?.find { it.id == idFuncion }
    }







    //@Query(
    //    "SELECT DISTINCT e.show " +
    //            "FROM Usuario u " +
    //            "JOIN u.amigos a " +
    //            "JOIN a.entradas e " +
    //            "WHERE u.id = :idUsuario"
    //)
    //fun obtenerShowsDeAmigos(idUsuario: Int): List<Show>

    //@Query(
    //    "SELECT DISTINCT s FROM Usuario u " +
    //            "JOIN u.amigos a " +
    //            "JOIN a.entradas e " +
    //            "JOIN e.show s " +
    //            "JOIN s.funciones f " +
    //            "WHERE u.id = :idUsuario " +
    //            "AND f.finalizo = false " +
    //            "AND LOWER(s.banda.nombre) LIKE LOWER(CONCAT('%', :artista, '%')) " +
    //            "AND LOWER(s.instalacion.nombre) LIKE LOWER(CONCAT('%', :locacion, '%'))"
    //)
    //fun findShowsDisponiblesDeAmigosByBandaNombreContainingIgnoreCaseAndInstalacionNombreContaining(
    //    idUsuario: Int,
    //    artista: String,
    //    locacion: String
    //): Set<Show>
}