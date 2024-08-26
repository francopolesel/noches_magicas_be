package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.Entrada
import ar.edu.unsam.phm.domain.Usuario
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EntradaRepository: CrudRepository<Entrada, Long>{
    override fun findById(id: Long): Optional<Entrada>
}