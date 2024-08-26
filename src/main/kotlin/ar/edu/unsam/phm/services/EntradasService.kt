package ar.edu.unsam.phm.services
import ar.edu.unsam.phm.domain.Businessexception
import ar.edu.unsam.phm.domain.Funcion
import ar.edu.unsam.phm.repository.EntradaRepository
import ar.edu.unsam.phm.repository.ShowRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class EntradasService (@Autowired val repoEntradas: EntradaRepository, @Autowired val repoShows: ShowRepository){

    fun findById(id : Long) = repoEntradas.findById(id).orElseThrow { Businessexception("La Entrada con ID $id no fue encontrada") }

    fun findFuncionById(id: String): Funcion {
        return repoShows.findFuncionByIdInShows(id)?:throw Businessexception("No se encontro al show")
    }
    fun fecha(idFuncion: String): LocalDateTime = findFuncionById(idFuncion).fecha

}