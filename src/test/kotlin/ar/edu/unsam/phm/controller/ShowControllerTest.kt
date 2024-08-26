package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.NochesMagicasApplication
import ar.edu.unsam.phm.domain.Filtro
import ar.edu.unsam.phm.services.ShowService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ContextConfiguration(classes = [NochesMagicasApplication::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Dado un controller de Show")
class ShowControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var showService: ShowService

    @Test
    fun `Se puede obtener la lista de shows, utilizando filtros`() {
        val id = 2
        val artista = "mgk"
        val locacion = "r"
        val conAmigos = false


        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/shows?idUsuario=$id&artista=$artista&locacion=$locacion&conAmigos=$conAmigos"
            )
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].banda").value("MGK"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombreInstalacion").value("River"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].amigosQueAsisten").isEmpty)
    }

    @Test
    fun `Se puede obtener la información del detalle de un show a partir de su id`() {
        val id = 2
        val idShow = showService.getShows(id.toLong(), Filtro())[3].id
        mockMvc.perform(MockMvcRequestBuilders.get("/detalleShow?idShow=$idShow&idUsuario=$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.banda").value("Duki"))
    }

    @Test
    fun `Se pueden obtener las estadísticas de un show en particular a partir de su id`() {
        val idAdmin = 1
        val idShow = showService.getShows(idAdmin.toLong(), Filtro())[1].id
        mockMvc.perform(MockMvcRequestBuilders.get("/estadisticasShow/$idShow?idAdmin=$idAdmin"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.costoTotal").value(153000.0))
    }

    @Test
    fun `Se puede obtener el listado de estados disponibles para un show`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/estados-show"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
    }

    @Test
    fun `Se pueden editar el estado de un show`() {
        val idAdmin = 1
        val idShow = showService.getShows(idAdmin.toLong(), Filtro())[2].id
        val nuevoEstado = "MEGA_SHOW"
        val nuevoNombre = "The Tour"
        mockMvc.perform(MockMvcRequestBuilders.post("/editar-show/$idShow?nuevoEstado=$nuevoEstado&nuevoNombre=$nuevoNombre&idAdmin=$idAdmin"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(MockMvcRequestBuilders.get("/estadisticasShow/$idShow?idAdmin=$idAdmin"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.estadoDeShow").value("MEGA_SHOW"))
    }
}