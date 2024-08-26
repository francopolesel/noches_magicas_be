package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.domain.Businessexception
import ar.edu.unsam.phm.repository.ComentarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ComentarioService(
    @Autowired val repoComentario: ComentarioRepository
) {

    fun findById(idComentario:Int)=
        repoComentario.findById(idComentario).orElseThrow { Businessexception("El comentario con ID $idComentario no fue encontrado")

}
}