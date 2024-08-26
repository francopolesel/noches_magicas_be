package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class ShowTest : DescribeSpec({


    val capacidadUbicacionGranRexPullman = CapacidadUbicacion(TipoUbicacion.PULLMAN, 200)
    val capacidadUbicacionRiverPlateaBaja = CapacidadUbicacion(TipoUbicacion.PLATEA_BAJA, 400)
    val ubicacionesGranRex = setOf(capacidadUbicacionGranRexPullman, capacidadUbicacionRiverPlateaBaja)
    describe("Show test") {
        val teatro = Teatro(
            "Teatro Gran Rex",
            -34.60, -58.38,
            ubicacionesGranRex,
            true
        )
        val mgk = Banda("MGK", "https://tinyurl.com/kbr9c6uh", 5000.0)
        val funcion1mgk = Funcion(LocalDateTime.of(2024, 3, 23, 21, 30, 0), ubicacionesGranRex)
        val showMGK = Show(mgk, teatro, "Downfalls High", mutableSetOf(funcion1mgk))

        val comentario1 = Comentario(2, "bien", 5.0, LocalDate.now())
        val comentario2 = Comentario(3, "bien", 1.0, LocalDate.now())
        showMGK.recibirComentario(comentario1)
        showMGK.recibirComentario(comentario2)

        it("el show recibe comentarios") {

            showMGK.listaComentarios.size.shouldBe(2)
        }

        it("el show calcula el rate") {

            showMGK.rate().shouldBe(3)
        }
        it("se pueden eiminar los comentarios") {
            showMGK.eliminarComentario(comentario1)
            showMGK.listaComentarios.size.shouldBe(1)
        }
        it("si no tiene comentarios el rate es 0") {
            showMGK.listaComentarios = mutableSetOf()
            showMGK.rate().shouldBe(0)
        }
        it("el show lleva la cuenta de subscripciones ") {
            showMGK.addSubscriptor()
            showMGK.suscriptores.shouldBe(1)
        }
    }
})