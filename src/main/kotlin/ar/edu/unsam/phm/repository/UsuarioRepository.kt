package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.Usuario
import ar.edu.unsam.phm.domain.UsuarioAdministrador
import ar.edu.unsam.phm.domain.UsuarioComun
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsuarioRepository : CrudRepository<Usuario, Long> {

    @EntityGraph(attributePaths = ["amigos", "entradas"])
    override fun findById(id: Long): Optional<Usuario>

    @EntityGraph(attributePaths = ["amigos", "entradas"])
    override fun findAll(): MutableIterable<Usuario>

    fun findByUsername(username: String): Usuario?

    @EntityGraph(attributePaths = ["amigos", "entradas"])
    fun getUsuarioComunById(id:Long):UsuarioComun

    @EntityGraph(attributePaths = ["amigos", "entradas"])
    fun getUsuarioAdminById(id:Long):UsuarioAdministrador

}