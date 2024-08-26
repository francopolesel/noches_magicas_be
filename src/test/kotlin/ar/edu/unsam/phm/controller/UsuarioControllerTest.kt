package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.NochesMagicasApplication
import ar.edu.unsam.phm.domain.Filtro
import ar.edu.unsam.phm.services.ShowService
import ar.edu.unsam.phm.services.UsuarioService
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ContextConfiguration(classes = [NochesMagicasApplication::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Dado un controller de Usuario")
class UsuarioControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val showService: ShowService,
    @Autowired val usuarioService: UsuarioService
) {

    @Test
    fun `Se puede obtener un usuario por su ID`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Fer"))
    }

    @Test
    fun `No se puede obtener un usuario por su ID`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/99"))
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "El Usuario con ID 99 no fue encontrado")
    }

    @Test
    fun `Se puede obtener el perfil de un usuario por su ID`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.esAdmin").value(true))
    }

    @Test
    fun `No se puede obtener el perfil de un usuario por su ID`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/99"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "El Usuario con ID 99 no fue encontrado")
    }

    @Test
    fun `Se puede actualizar el perfil de un usuario`() {
        val id = 1
        val usuarioPerfilDTO = """
        {
            "nombre" : "Fede",
            "apellido" : "Boselli",
            "imagen" : "sebaboselli.jpg",
            "fechaDeNacimiento" : "1990-01-01",
            "credito" : 0.0,
            "esAdmin" : false
        }
        """

        mockMvc.perform(
            MockMvcRequestBuilders.put("/actualizar-usuario/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioPerfilDTO)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Fede"))
    }

    @Test
    fun `Se le puede sumar credito a un usuario`() {
        val id = 1
        val cantidad = """
        {
            "cantidad": 100.0
        }
        """

        mockMvc.perform(
            MockMvcRequestBuilders.put("/sumar-credito/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cantidad)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
        mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.credito").value(1000099.0))

    }


    @Test
    fun `Se pueden obtener los amigos del usuario`() {
        val id = 2
        mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/amigos?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
    }

    @Test
    fun `Se pueden eliminar amigos del usuario`() {
        val id = 2
        val idAmigo = 3
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAmigo?idUsuario=$id&idAmigo=$idAmigo"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/amigos?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
    }

    @Test
    fun `Se pueden obtener las entradas del usuario`() {
        val id = 2
        mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/entradas?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
    }

    @Test
    fun `Se pueden agregar entradas al carrito del usuario, levantar la informacion del mismo y luego limpiarlo`() {
        val id = 2
        val idShow = showService.getShowsDisponibles(id.toLong(), Filtro())[0].id
        val funcionId = showService.getShowById(idShow, id.toLong()).detalleFunciones[0].id
        print(funcionId)
        val entradas = """
        [
            {
                "id": "$funcionId",
                "cantidad": 2,
                "ubicacion": "PALCO"
            }
        ]

        """.trimIndent()
        val idUsuario = """
            $id
        """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/agregar-entradas-carrito/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(entradas)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/cart?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/clearCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(idUsuario)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/cart?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
    }


    @Test
    fun `Se pueden comprar las entradas que estan en el carrito`() {
        val id = 2
        val idShow = showService.getShowsDisponibles(id.toLong(), Filtro())[0].id
        val funcionId = showService.getShowById(idShow, id.toLong()).detalleFunciones[0].id
        val entradas = """
         [
            {
                "id": "$funcionId",
                "cantidad": 2,
                "ubicacion": "PALCO"
            }
        ]
        """.trimIndent()
        val idUsuario = """
            $id
        """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/agregar-entradas-carrito/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(entradas)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/comprar-entradas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(idUsuario)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/cart?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil-usuario/entradas?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4))
    }


    @Test
    fun `Se puede comentar y eliminar un comentario en un show`() {
        val id = 2
        val idComentario = 2
        val idEntrada = usuarioService.getEntradasCompradas(id.toLong()).filter { it.puedeCalificar }[0].id
        val comentario = """
            {
                "idUsuario": $id,
                "idEntrada": $idEntrada,
                "mensaje": "Muy bueno",
                "calificacion": 3.0,
                "fecha": "2024-04-04"
            }
            """.trimIndent()

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil/comentarios?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/comentarShow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comentario)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil/comentarios?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))

        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteComentario?idComentario=$idComentario"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil/comentarios?idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

    }

}
