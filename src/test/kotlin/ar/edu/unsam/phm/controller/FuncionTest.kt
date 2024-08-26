package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.domain.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class FuncionTest : DescribeSpec({

    describe("Funcion test") {
        val capacidadUbicacionGranRexPullman = CapacidadUbicacion(TipoUbicacion.PULLMAN, 200)
        val capacidadUbicacionRiverPlateaBaja = CapacidadUbicacion(TipoUbicacion.PLATEA_BAJA, 400)
        val ubicacionesGranRex = setOf(capacidadUbicacionGranRexPullman, capacidadUbicacionRiverPlateaBaja)
        val teatro = Teatro(
            "Teatro Gran Rex",
            -34.60,
            -58.38,
            ubicacionesGranRex,
            true,
        )

        val fechaNacimiento1 = LocalDate.of(2024, 3, 14)
        val mgk = Banda("MGK", "https://tinyurl.com/kbr9c6uh", 5000.0)
        val funcion1mgk = Funcion(LocalDateTime.of(2024, 3, 23, 21, 30, 0), ubicacionesGranRex)
//        var showMGK = Show(mgk, teatro, "Downfalls High", mutableSetOf(funcion1mgk))
        val entradaPlateaAlta1 = Entrada(1, TipoUbicacion.PLATEA_BAJA, "funcion1mgk", "showMGK")

        val usuarioNormal = UsuarioComun("Juan", "Pérez", fechaNacimiento1, "juanito", "contraseña123")
//        val usuarioNormal2 = UsuarioComun("numero", "dos", fechaNacimiento1, "juanito", "contraseña123")


        it("la funcion lleva la cuenta de las entradas vendidas") {
            val cantidadActual = funcion1mgk.entradasDisponiblesPorUbicacion(entradaPlateaAlta1.tipoUbicacion)
            funcion1mgk.venderEntrada(entradaPlateaAlta1, usuarioNormal,500.0)
            funcion1mgk.entradasDisponiblesPorUbicacion(entradaPlateaAlta1.tipoUbicacion).shouldBe(cantidadActual - 1)

        }
        it("la funcion lleva la cuenta de las entradas disponibles") {
            val cantidadActual = funcion1mgk.entradasDisponiblesTotales()

            funcion1mgk.venderEntrada(entradaPlateaAlta1, usuarioNormal,500.0)

            funcion1mgk.entradasDisponiblesTotales().shouldBe(cantidadActual - 1)

        }
        it("si no quedan de ese tipo de entrada a comprar no te deja comprar entrada") {
            funcion1mgk.simularSoldOut()
            shouldThrow<Businessexception> {
                funcion1mgk.venderEntrada(entradaPlateaAlta1, usuarioNormal,500.0)
            }
        }
        it("la funcion sabe decirte si estas soldout") {

            funcion1mgk.simularSoldOut()

            funcion1mgk.soldOut().shouldBe(true)
        }
        it("una funcion de ayer ya paso") {

            funcion1mgk.fecha = LocalDateTime.now().minusDays(1)

            funcion1mgk.finalizo().shouldBe(true)
        }


    }
})