package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.Comentario
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ComentarioRepository: CrudRepository<Comentario, Int>{

    fun getComentarioByUsuarioId(id: Long):List<Comentario>
}