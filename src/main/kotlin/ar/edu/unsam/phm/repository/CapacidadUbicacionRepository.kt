package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.CapacidadUbicacion
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CapacidadUbicacionRepository: CrudRepository<CapacidadUbicacion, Long> {
}