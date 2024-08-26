package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.Banda
import ar.edu.unsam.phm.domain.Entrada
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BandaRepository : CrudRepository<Banda, Long> {
}