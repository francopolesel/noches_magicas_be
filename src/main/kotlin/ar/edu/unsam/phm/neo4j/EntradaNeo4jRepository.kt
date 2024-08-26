package ar.edu.unsam.phm.neo4j

import ar.edu.unsam.phm.domain.Entrada
import org.springframework.stereotype.Repository
import org.springframework.data.neo4j.repository.Neo4jRepository
@Repository
interface EntradaNeo4jRepository: Neo4jRepository<Entrada, Long> {
}