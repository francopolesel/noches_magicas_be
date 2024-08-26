package ar.edu.unsam.phm.neo4j

import ar.edu.unsam.phm.domain.Usuario
import org.springframework.stereotype.Repository
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param

@Repository
interface UsuarioNeo4jRepository: Neo4jRepository<Usuario, Long> {

    @Query("MATCH (u:Usuario)-[:AMIGO_DE]-(amigo)-[:AMIGO_DE]-(amigoDeAmigo)-[:ASISTIO_A]->(entrada) WHERE u.id = \$idUsuario AND NOT (u)-[:AMIGO_DE]-(amigoDeAmigo) AND EXISTS { MATCH (u)-[:ASISTIO_A]->(e) WHERE entrada.idShow = e.idShow } RETURN amigoDeAmigo")
    fun findAmigosSugeridos(@Param("idUsuario") idUsuario: Long): List<Usuario>

    @Query("MATCH (u:Usuario)-[r:AMIGO_DE]-(a:Usuario) WHERE u.id = \$idUsuario AND a.id = \$idAmigo DELETE r")
    fun eliminarRelacionAmigo(@Param("idUsuario") idUsuario: Long, @Param("idAmigo") idAmigo: Long)

    @Query("MATCH (u:Usuario {id: \$idUsuario}), (e:Entrada) WHERE ID(e) = \$idEntrada CREATE (u)-[:ASISTIO_A]->(e)")
    fun comprarEntrada(@Param("idUsuario") idUsuario: Long, @Param("idEntrada") idEntrada: Long)

}