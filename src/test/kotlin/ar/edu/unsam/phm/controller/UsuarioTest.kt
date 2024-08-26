package ar.edu.unsam.phm.controller

import ar.edu.unsam.phm.domain.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class UsuarioTest : DescribeSpec({

    describe("Usuarios test") {

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
        val usuarioAdministrador = UsuarioAdministrador("Juan", "Pérez", fechaNacimiento1, "juanito", "contraseña123")
        val usuarioNormal = UsuarioComun("Juan", "Pérez", fechaNacimiento1, "juanito", "contraseña123")
        val usuarioNormal2 = UsuarioComun("numero", "dos", fechaNacimiento1, "juanito", "contraseña123")
        val funcion2mgk = Funcion(LocalDateTime.of(2024, 3, 24, 21, 30, 0), ubicacionesGranRex)
        val showMGK =
            Show(mgk, teatro, "Downfalls High", mutableSetOf(funcion1mgk, funcion2mgk)).apply { id = "showMGK" }
        val entradaPlateaBaja = Entrada(1, TipoUbicacion.PLATEA_BAJA, "funcion1mgk", "showMGK")
        val entradaPullman = Entrada(2, TipoUbicacion.PULLMAN, "funcion2mgk", "showMGK")
        val entradaPlateaAlta1 = Entrada(1, TipoUbicacion.PLATEA_BAJA, "funcion1mgk", "showMGK")
        val duki = Banda(
            "Duki",
            "https://pxccdn.ciudadano.news/ciudadano/112020/1604534810371/el_duki_crop1575678855917.jpg_554688468.jpg",
            1000.0
        )

        val capacidadUbicacionRiverPalco = CapacidadUbicacion(TipoUbicacion.PALCO, 12)
        val capacidadUbicacionRiverPlateaAlta = CapacidadUbicacion(TipoUbicacion.PLATEA_ALTA, 300)
        val capacidadUbicacionRiverCampo = CapacidadUbicacion(TipoUbicacion.CAMPO, 2000)

        val ubicacionesRiver =
            setOf(capacidadUbicacionRiverPalco, capacidadUbicacionRiverPlateaAlta, capacidadUbicacionRiverCampo)

        val river = Estadio("River", 34.543, -58.449, ubicacionesRiver, 2000.0)

        val funcionDuko1SoldOut = Funcion(LocalDateTime.of(2025, 3, 23, 21, 30, 0), ubicacionesRiver)
        val showDuko = Show(duki, river, "Modo Diablo Tour", mutableSetOf(funcionDuko1SoldOut)).apply {
            suscriptores = 1000
        }
        val entradaCampoDuki1 = Entrada(15, TipoUbicacion.CAMPO, "funcionDuko1SoldOut", "showDuko")
        val usuarioNormalTestAdmin = UsuarioComun("Juan", "Pérez", fechaNacimiento1, "juanito", "contraseña123")
        val usuarioNormal2TestAdmin = UsuarioComun("numero", "dos", fechaNacimiento1, "juanito", "contraseña123")

        val carrito = CarritoDeCompras(0)

        describe("usuariosNormales Tests") {


            it("el usuario normal puede agregar amigos a su lista") {
                usuarioNormal.agregarAmigo(usuarioNormal2)

                usuarioNormal.amigos.shouldContain(usuarioNormal2)

            }
            it("el usuario normal puede eliminar amigos de su lista") {

                usuarioNormal.eliminarAmigo(usuarioNormal2)

                usuarioNormal.amigos.shouldNotContain(usuarioNormal2)
            }
            it("el usuario normal deberia poder compra entrada y se le resta el costo") {

                val saldoAnterior = usuarioNormal.saldo
                usuarioNormal.comprarEntrada(entradaPlateaAlta1, 500.0)
                usuarioNormal.saldo.shouldBe(saldoAnterior - 500.0)
            }
            it("si el usuario no tiene saldo no deberia saltar un error") {

                usuarioNormal.saldo = 0.0
                shouldThrow<Businessexception> { usuarioNormal.comprarEntrada(entradaPlateaAlta1, 500.0) }
            }
            it("el usuario normal puede cargar saldo") {

                val saldoAnterior = usuarioNormal.saldo
                val saldoAgregado = 100.0
                usuarioNormal.agregarSaldo(saldoAgregado)
                usuarioNormal.saldo.shouldBe(saldoAnterior + saldoAgregado)
            }
            it("el usuario normal puede saber si el amigo asiste al show") {
                usuarioNormal.agregarAmigo(usuarioNormal2)
                usuarioNormal2.comprarEntrada(entradaPullman, 500.0)
                usuarioNormal.amigosQueAsisten(showMGK).toList().shouldBe(listOf(usuarioNormal2))
            }

        }

        describe("Usuario Administrador tests") {

            beforeEach {
                funcion2mgk.venderEntrada(entradaPullman, usuarioNormalTestAdmin, 500.0)
                funcion1mgk.venderEntrada(entradaPullman, usuarioNormal2TestAdmin, 1000.0)
            }

            it("El administrador puede obtener los ingresos totales de una funcion determinada") {
                val precioEntradaPullman = showMGK.precioDeEntrada(entradaPullman.tipoUbicacion)
                usuarioAdministrador.ingresosTotalFuncion(funcion2mgk, showMGK).shouldBe(precioEntradaPullman)
            }

            it("A partir de las funciones del show, el administrador puede obtener los ingresos totales del show") {
                val totalEntradas = showMGK.precioDeEntrada(entradaPullman.tipoUbicacion) * 4
                usuarioAdministrador.ingresosTotalShow(mutableSetOf(funcion1mgk, funcion2mgk), showMGK)
                    .shouldBe(totalEntradas)
            }

            it("A partir de las funciones, el administrador puede obtener las entradas vendidas totales de cada tipo del show") {
                usuarioAdministrador.totalEntradasVendidasPorTipo(setOf(funcion1mgk, funcion2mgk))
                    .shouldBe(listOf(Pair(TipoUbicacion.PULLMAN, 6), Pair(TipoUbicacion.PLATEA_BAJA, 0)))
            }

            it("A partir de las funciones, el administrador puede obtener las entradas vendidas totales del show") {
                usuarioAdministrador.totalEntradasVendidasShow(setOf(funcion1mgk, funcion2mgk)).shouldBe(8)
            }

            it("Si un show tiene sus funciones soldout y los suscriptores al mismo cubren el costo a partir del precio de la entrada mas baja, el administrador puede crear una funcion") {
                val entradaMasBarataDuko = showDuko.entradaMasBarata()
                val costoShowDuko = showDuko.costoDelShow()
                val suscriptoresShowDuko = showDuko.suscriptores
                funcionDuko1SoldOut.simularSoldOut()

                usuarioAdministrador.puedoCrearFuncion(
                    setOf(funcionDuko1SoldOut),
                    suscriptoresShowDuko,
                    entradaMasBarataDuko.toDouble(),
                    costoShowDuko
                ).shouldBe(true)
            }

            it("Si un show tiene sus funciones soldout y los suscriptores al mismo no cubren el costo a partir del precio de la entrada mas baja, el administrador no puede crear una funcion") {
                val entradaMasBarataDuko = showDuko.entradaMasBarata()
                val costoShowDuko = showDuko.costoDelShow()
                showDuko.suscriptores = 0
                val suscriptoresShowDuko = showDuko.suscriptores
                funcionDuko1SoldOut.simularSoldOut()

                usuarioAdministrador.puedoCrearFuncion(
                    setOf(funcionDuko1SoldOut),
                    suscriptoresShowDuko,
                    entradaMasBarataDuko.toDouble(),
                    costoShowDuko
                ).shouldBe(false)
            }

            it("El administrador puede cambiar el estado de un show") {
                val nuevoEstado = EstadoDeShow.MEGA_SHOW
                usuarioAdministrador.cambiarEstadoDeUnShow(showMGK, nuevoEstado)

                showMGK.estado.shouldBe(nuevoEstado)
            }

        }
    }
})