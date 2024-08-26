package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.domain.Credenciales
import ar.edu.unsam.phm.domain.Retorno
import ar.edu.unsam.phm.domain.Usuario
import ar.edu.unsam.phm.domain.UsuarioComun
import ar.edu.unsam.phm.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"], methods = [RequestMethod.POST, RequestMethod.OPTIONS])
class AuthController(
    @Autowired val authService: AuthService,
) {

    @PostMapping("/login")
    @Operation(summary = "Recibe la contraseña y el id del usuario y si coinicden con los registros devuelve el ID")
    fun validarUsuario(@RequestBody request: Credenciales): Retorno {
        val username = request.username
        val password = request.password
        return authService.validarUsername(username, password)
    }

    @PostMapping("/signup")
    @Operation(summary = "Recibe los datos del cliente para registrarlo")
    fun registrarUsuario(@RequestBody usuario: UsuarioComun): Usuario = authService.registrarUsuario(usuario)

}