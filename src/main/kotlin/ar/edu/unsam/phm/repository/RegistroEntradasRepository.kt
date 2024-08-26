package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.RegistroEntradas
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RegistroEntradasRepository : CrudRepository<RegistroEntradas, Long> {
}