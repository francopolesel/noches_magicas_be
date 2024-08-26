package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.domain.ClickData
import ar.edu.unsam.phm.repository.ClickRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ClickService(
    @Autowired val repoClick: ClickRepository,
    @Autowired val usuarioService: UsuarioService,
    @Autowired val showService: ShowService
) {

    fun registrarClick(idShow: String, idUsario: Long) {
        val usuario = usuarioService.findById(idUsario)
        val show = showService.findById(idShow)
        val clickData = ClickData(LocalDateTime.now(), show.id, show.evento, usuario.nombreCompleto(), usuario.id)
        repoClick.save(clickData)
    }
}