package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.Instalacion
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InstalacionRepository : CrudRepository<Instalacion, Long> {
    @EntityGraph(attributePaths = ["capacidad"]) //LO UTILIZO PARA MARCAR QUE CAMPOS QUIERO UTILIZAR EN FORMA EAGER PARA NO TENER PROBLEMAS DEL LAZY N+1
    override fun findById(id: Long): Optional<Instalacion> //AL USAR ENTITYGRAPH ES FLEXIBLE, NO SE MODIFICA LA ENTIDAD PRINCIPAL Y SOLO ES EAGER PARA LAS CONSULTAS DEL REPO

}