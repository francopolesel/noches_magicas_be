package ar.edu.unsam.phm.services

import ar.edu.unsam.phm.domain.Businessexception
import ar.edu.unsam.phm.domain.PasswordUnmatchException
import ar.edu.unsam.phm.domain.Retorno
import ar.edu.unsam.phm.domain.Usuario
import ar.edu.unsam.phm.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthService(@Autowired val repoUsuarios: UsuarioRepository,@Autowired val usuarioService: UsuarioService) {

    fun validarUsername(username: String, pwd: String): Retorno {
        val idUsuario = repoUsuarios.findByUsername(username)?.id ?: throw Businessexception("Sus credenciales no coinciden, intente nuevamente")
        val usuario = usuarioService.findById(idUsuario)
        val esAdmin = usuario.esAdministrador()

        return if (usuario.password == pwd) Retorno(idUsuario, esAdmin) else
            throw PasswordUnmatchException("Sus credenciales no coinciden, intente nuevamente")
    }

    @Transactional
    fun registrarUsuario(usuario: Usuario):Usuario {
        usuarioService.validarExistenciaByUsername(usuario.username)
        return repoUsuarios.save(usuario)
    }
}