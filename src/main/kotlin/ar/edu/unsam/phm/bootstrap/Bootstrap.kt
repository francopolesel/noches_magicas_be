package ar.edu.unsam.phm.bootstrap

//import ar.edu.unsam.phm.repository.EntradaRepository
import ar.edu.unsam.phm.DTO.ShowMongoDTO
import ar.edu.unsam.phm.DTO.toShowMongoDTO
import ar.edu.unsam.phm.domain.*
import ar.edu.unsam.phm.repository.*
import ar.edu.unsam.phm.neo4j.EntradaNeo4jRepository
import ar.edu.unsam.phm.neo4j.UsuarioNeo4jRepository
import ar.edu.unsam.phm.services.CarritoService
import ar.edu.unsam.phm.services.FuncionService
import ar.edu.unsam.phm.services.UsuarioService
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class Bootstrap(
    val repositorioUsuario: UsuarioRepository,
    val repositorioUsuarioNeo4j: UsuarioNeo4jRepository,
    val repositorioBandas: BandaRepository,
    val repositorioInstalaciones: InstalacionRepository,
    val repositorioShow: ShowRepository,
    val repositorioCapacidadUbicacion: CapacidadUbicacionRepository,
    val repositorioComentario: ComentarioRepository,
    val carritoRepository: CarritoRepository,
    val repositorioEntradas: EntradaNeo4jRepository,
    val repositorioEntradasJPA: EntradaRepository,
    @Autowired val carritoService: CarritoService,
    @Autowired val usuarioService: UsuarioService,


    ) : InitializingBean {
    // Bandas
    val mgk =
        BandaBuilder().nombre("MGK").imagen("https://fmaspen.com/wp-content/uploads/2023/06/Machine-Gun-Kelly.jpg")
            .costo(5000.0).build()
    val jonas = BandaBuilder().nombre("jonas brothers")
        .imagen("https://elplanetaurbano.com/wp-content/uploads/2023/12/Jonas-Brothers-1.jpg").costo(3000.0).build()
    val postMalone = BandaBuilder().nombre("post malone")
        .imagen("https://s.yimg.com/ny/api/res/1.2/6jYTVRYnAa4yyvWp5CVjBA--/YXBwaWQ9aGlnaGxhbmRlcjt3PTY0MDtoPTQyNw--/https://media.zenfs.com/en/rollingstone.com/b43be62785b85d5e294d67a045a77b23")
        .costo(4500.0).build()

    val duki = BandaBuilder().nombre("Duki")
        .imagen("https://tn.com.ar/resizer/eb_N3dqeYTxoEv_fmGJYX339DMU=/1440x0/smart/filters:format(webp)/cloudfront-us-east-1.images.arcpublishing.com/artear/6FCTDSPW5VGULB3J2UUVBC3AKU.jpg")
        .costo(1000.0).build()
    val tanBionica = BandaBuilder().nombre("tan bionica")
        .imagen("https://d34ugyblrhxy34.cloudfront.net/wp-content/uploads/2023/06/12123445/5-Tan-Bionica-scaled.jpg")
        .costo(1360.0).build()

//Capacidades de Instalaciones

    val capacidadUbicacionRiverPalco = CapacidadUbicacion(TipoUbicacion.PALCO, 12)
    val capacidadUbicacionRiverPlateaAlta = CapacidadUbicacion(TipoUbicacion.PLATEA_ALTA, 300)
    val capacidadUbicacionRiverCampo = CapacidadUbicacion(TipoUbicacion.CAMPO, 2000)

    val ubicacionesRiver =
        setOf(capacidadUbicacionRiverPalco, capacidadUbicacionRiverPlateaAlta, capacidadUbicacionRiverCampo)

    val capacidadUbicacionGranRexPullman = CapacidadUbicacion(TipoUbicacion.PULLMAN, 200)
    val capacidadUbicacionRiverPlateaBaja = CapacidadUbicacion(TipoUbicacion.PLATEA_BAJA, 400)

    val ubicacionesGranRex = setOf(capacidadUbicacionGranRexPullman, capacidadUbicacionRiverPlateaBaja)

    val capacidadUbicacionVelezPalco = CapacidadUbicacion(TipoUbicacion.PALCO, 15)
    val capacidadUbicacionVelezPlateaAlta = CapacidadUbicacion(TipoUbicacion.PLATEA_ALTA, 350)
    val capacidadUbicacionVelezCampo = CapacidadUbicacion(TipoUbicacion.CAMPO, 3000)

    val ubicacionesVelez =
        setOf(capacidadUbicacionVelezPalco, capacidadUbicacionVelezPlateaAlta, capacidadUbicacionVelezCampo)

//Instalaciones

    val river = InstalacionBuilder().nombre("River").capacidad(ubicacionesRiver).costoFijo(2000.0).coordenadaX(-34.543)
        .coordenadaY(-58.449).buildEstadio()
    val granRex =
        InstalacionBuilder().nombre("Gran Rex").capacidad(ubicacionesGranRex).buenaAcustica(true).coordenadaX(-34.601)
            .coordenadaY(-58.377).buildTeatro()
    val velez = InstalacionBuilder().nombre("Velez").capacidad(ubicacionesVelez).costoFijo(2500.0).coordenadaX(-35.563)
        .coordenadaY(-78.246).buildEstadio()

    //Usuarios

    var usuarioAdmin = UsuarioBuilder().nombre("Fer").apellido("Dodino").fechaNacimiento(
        LocalDate.of(
            1979, 9, 12
        )
    ).username("admin").password("Admin").buildAdministrador()

    var usuarioNormalJuan = UsuarioBuilder().nombre("Juan").apellido("Lopez").fechaNacimiento(
        LocalDate.of(
            1999, 9, 12
        )
    ).username("juan123").password("1234").buildComun()
    var usuarioNormalTopa = UsuarioBuilder().nombre("Topa").apellido("Mate").fechaNacimiento(
        LocalDate.of(
            1999, 9, 12
        )
    ).username("topa123").password("1234").buildComun()

    var usuarioNormalMiguel = UsuarioBuilder().nombre("Miguel").apellido("Borja").fechaNacimiento(
        LocalDate.of(
            1999, 9, 12
        )
    ).username("miguel").password("1234").buildComun()

    var usuarioNormalFranco = UsuarioBuilder().nombre("Franco").apellido("Armani").fechaNacimiento(
        LocalDate.of(
            1999, 9, 12
        )
    ).username("fran123").password("1234").buildComun()

    var usuarioNormalTiburon = UsuarioBuilder().nombre("El").apellido("Tuburon").fechaNacimiento(
        LocalDate.of(
            1999, 9, 12
        )
    ).username("el123").password("1234").buildComun()

//Funciones

    val funcion1mgk =
        FuncionBuilder().fecha(LocalDateTime.of(2024, 7, 23, 21, 30, 0)).tipoDeEntrada(ubicacionesRiver).build()
    val funcion2mgk =
        FuncionBuilder().fecha(LocalDateTime.of(2024, 7, 25, 22, 30, 0)).tipoDeEntrada(ubicacionesRiver).build()
    val funcion1jonas =
        FuncionBuilder().fecha(LocalDateTime.of(2024, 8, 23, 21, 0, 0)).tipoDeEntrada(ubicacionesGranRex).build()
    val funcion1postMalone =
        FuncionBuilder().fecha(LocalDateTime.of(2024, 9, 23, 20, 0, 0)).tipoDeEntrada(ubicacionesVelez).build()
    val funcion2postMalone =
        FuncionBuilder().fecha(LocalDateTime.of(2025, 3, 23, 21, 30, 0)).tipoDeEntrada(ubicacionesVelez).build()

    val funcionDuko1SoldOut =
        FuncionBuilder().fecha(LocalDateTime.of(2025, 3, 23, 21, 30, 0)).tipoDeEntrada(ubicacionesRiver).build()
            .apply { simularSoldOut() }
    val funciontbTerminada =
        FuncionBuilder().fecha(LocalDateTime.of(2024, 3, 23, 22, 30, 0)).tipoDeEntrada(ubicacionesGranRex).build()


//Comentarios

    lateinit var comentarioAdmin: Comentario
    lateinit var comentario1: Comentario
    lateinit var comentario2: Comentario
    lateinit var comentario3: Comentario
    lateinit var comentario4: Comentario
    lateinit var comentario5: Comentario
    lateinit var comentario6: Comentario
    lateinit var comentario7: Comentario

    //DTO
    lateinit var showMGK: ShowMongoDTO
    lateinit var showJonas: ShowMongoDTO
    lateinit var showPostMalone: ShowMongoDTO
    lateinit var showDuko: ShowMongoDTO
    lateinit var showTbCerrado: ShowMongoDTO

    fun crearUsuarios() = repositorioUsuario.apply {
        val usuarios = listOf(
            usuarioAdmin,
            usuarioNormalJuan,
            usuarioNormalTopa,
            usuarioNormalMiguel,
            usuarioNormalFranco,
            usuarioNormalTiburon
        )

        // Configura las relaciones de amistad
        usuarioNormalTopa.agregarAmigo(usuarioNormalFranco)
        usuarioNormalFranco.agregarAmigo(usuarioNormalTopa)
        usuarioNormalJuan.agregarAmigo(usuarioNormalTopa)
        usuarioNormalJuan.agregarAmigo(usuarioNormalTiburon)
        usuarioNormalJuan.agregarAmigo(usuarioNormalMiguel)


        // Se les asigna imagenes a los usuarios
        usuarioAdmin.cambiarImagen("https://www.directvsports.com/__export/1693011328512/sites/dsports/img/2023/08/25/20230825_095528087_17qv2pq3w3wdd1vgvkahi758sb.jpg_1301049368.jpg")
        usuarioNormalJuan.cambiarImagen("https://img.asmedia.epimg.net/resizer/v2/Z7QEBVVZHFUH6DJYIM3K6KIH7E.jpg?auth=0c7b6e8e24b59f1e4ba3388b3c297992807083b7aba60f05b6ce695ed6e2dd11&width=1472&height=828&focal=1470%2C274")
        usuarioNormalTopa.cambiarImagen("https://pbs.twimg.com/media/Cci0OvVW0AA4kBi.jpg:large")
        usuarioNormalMiguel.cambiarImagen("https://tntsports.com.ar/__export/1709125729483/sites/tntsports/img/2024/02/05/miguel_borja.jpg")
        usuarioNormalFranco.cambiarImagen("https://laseleccionargentina.com/wp-content/uploads/2024/03/Franco-Armani-768x453.png")
        usuarioNormalTiburon.cambiarImagen("https://aldosivi.com/images/contents/main_5de6a0d4da2f6.jpeg")

        // Guarda todos los usuarios en una sola operación
        repositorioUsuario.saveAll(usuarios)
    }

    fun crearUsuariosNeo4j() {
        repositorioUsuarioNeo4j.deleteAll()

        val usuarios = listOf(
            usuarioAdmin,
            usuarioNormalJuan,
            usuarioNormalTopa,
            usuarioNormalMiguel,
            usuarioNormalFranco,
            usuarioNormalTiburon
        )

        repositorioUsuarioNeo4j.saveAll(usuarios)
    }


    fun usuariosCompranEntradas() {


        var entradaPlateaAltaMgk1 = Entrada(1, TipoUbicacion.PLATEA_ALTA, funcion1mgk.id, showMGK.id)
        var entradaPalcoMgk1 = Entrada(7, TipoUbicacion.PALCO, funcion1mgk.id, showMGK.id)
        var entradaPalcoMgk2 = Entrada(9, TipoUbicacion.PALCO, funcion2mgk.id, showMGK.id)
        var entradaPullmanTb1 = Entrada(19, TipoUbicacion.PULLMAN, funciontbTerminada.id, showTbCerrado.id)

        repositorioEntradas.apply { deleteAll() }
        repositorioUsuarioNeo4j.deleteAll()

        val usuarios = listOf(
            usuarioAdmin,
            usuarioNormalJuan,
            usuarioNormalTopa,
            usuarioNormalMiguel,
            usuarioNormalFranco,
            usuarioNormalTiburon
        )

        repositorioUsuarioNeo4j.saveAll(usuarios)

        entradaPlateaAltaMgk1 = repositorioEntradas.save(entradaPlateaAltaMgk1)
        entradaPalcoMgk1 = repositorioEntradas.save(entradaPalcoMgk1)
        entradaPalcoMgk2 = repositorioEntradas.save(entradaPalcoMgk2)
        entradaPullmanTb1 = repositorioEntradas.save(entradaPullmanTb1)
        usuarioNormalJuan.entradas.add(entradaPlateaAltaMgk1)
        usuarioNormalJuan.entradas.add(entradaPullmanTb1)
        usuarioNormalFranco.entradas.add(entradaPalcoMgk1)
        usuarioNormalFranco.entradas.add(entradaPalcoMgk2)
        repositorioUsuarioNeo4j.comprarEntrada(usuarioNormalJuan.id, entradaPlateaAltaMgk1.id)
        repositorioUsuarioNeo4j.comprarEntrada(usuarioNormalJuan.id, entradaPullmanTb1.id)
        repositorioUsuarioNeo4j.comprarEntrada(usuarioNormalFranco.id, entradaPalcoMgk1.id)
        repositorioUsuarioNeo4j.comprarEntrada(usuarioNormalFranco.id, entradaPalcoMgk2.id)

        repositorioUsuario.apply {
            save(usuarioNormalJuan)
            save(usuarioNormalFranco)
        }

    }

    fun crearShows() = repositorioShow.apply {
        deleteAll()
        saveComentarios()
        val mgk = ShowBuilder().banda(mgk).evento("Downfalls High").instalacion(river).agregarFuncion(funcion2mgk)
            .agregarFuncion(funcion1mgk).build().apply {
                recibirComentario(comentario1)
                recibirComentario(comentario2)
                recibirComentario(comentario3)
                recibirComentario(comentario4)
                recibirComentario(comentario5)
            }
        showMGK = save(mgk.toShowMongoDTO())

        val jonas =
            ShowBuilder().agregarFuncion(funcion1jonas).banda(jonas).instalacion(granRex).evento("The Tour").build()
                .apply {
                    recibirComentario(comentario6)
                    recibirComentario(comentario7)
                }
        showJonas = save(jonas.toShowMongoDTO())

        val post =
            ShowBuilder().banda(postMalone).evento("Bad vibes").instalacion(velez).agregarFuncion(funcion2postMalone)
                .agregarFuncion(funcion1postMalone).build()
        showPostMalone = save(post.toShowMongoDTO())

        val duko =
            ShowBuilder().banda(duki).instalacion(river).evento("Modo diablo tour").agregarFuncion(funcionDuko1SoldOut)
                .build().apply {
                    suscriptores = 1000
                }
        showDuko = save(duko.toShowMongoDTO())


        val tbCerrado = ShowBuilder().banda(tanBionica).instalacion(granRex).evento("La ultima noche magica")
            .agregarFuncion(funciontbTerminada).build()
        Show(tanBionica, granRex, "La Ultima Noche Magica", mutableSetOf(funciontbTerminada))
        showTbCerrado = save(tbCerrado.toShowMongoDTO())
    }

    fun crear400Shows() {
        val show400 = ShowBuilder().build()
        repeat(400) {
            repositorioShow.apply { save(show400.toShowMongoDTO()) }
        }
    }

    fun actualizarShows() {
        repositorioShow.apply {
            save(showMGK)
            save(showJonas)
            save(showPostMalone)
            save(showDuko)
            save(showTbCerrado)
        }

    }

    fun saveComentarios() {

        repositorioComentario.apply {
            comentarioAdmin = save(
                Comentario(
                    usuarioAdmin.id, "Muy buen recital, mucha gente, la pase bomba", 5.0, LocalDate.of(2024, 6, 20)
                )
            )
            comentario1 = save(
                Comentario(
                    usuarioNormalJuan.id,
                    "Estuvo bueno, el volumen podria haber sido mas alto",
                    4.0,
                    LocalDate.of(2024, 7, 20)
                )
            )
            comentario2 =
                save(Comentario(usuarioNormalTopa.id, "Malisimo, de lo peor que vi", 1.0, LocalDate.of(2024, 9, 20)))
            comentario3 =
                save(
                    Comentario(
                        usuarioNormalMiguel.id,
                        "Bastante bien, vale su precio",
                        3.8,
                        LocalDate.of(2024, 6, 20)
                    )
                )
            comentario4 =
                save(Comentario(usuarioNormalFranco.id, "Estuvo bueno, me encanto", 5.0, LocalDate.of(2024, 7, 20)))
            comentario5 =
                save(
                    Comentario(
                        usuarioNormalTiburon.id,
                        "Meh, podría haber sido mejor",
                        3.0,
                        LocalDate.of(2024, 7, 20)
                    )
                )
            comentario6 = save(
                Comentario(
                    usuarioNormalFranco.id,
                    "Increíble experiencia, volvería sin dudarlo",
                    4.8,
                    LocalDate.of(2024, 8, 15)
                )
            )
            comentario7 = save(
                Comentario(
                    usuarioNormalTiburon.id,
                    "El mejor show que vi en mi vida, sin palabras",
                    5.0,
                    LocalDate.of(2024, 8, 28)
                )
            )
        }
    }

    fun limpiarEntradas() = repositorioEntradasJPA.apply { deleteAll() }

    fun crearBandas() = repositorioBandas.apply {

        save(mgk)
        save(jonas)
        save(postMalone)
        save(duki)
        save(tanBionica)

    }

    fun crearInstalaciones() = repositorioInstalaciones.apply {
        save(river)
        save(granRex)
        save(velez)
    }

    fun crearUbicaciones() = repositorioCapacidadUbicacion.apply {
        ubicacionesRiver.forEach { save(it) }
        ubicacionesVelez.forEach { save(it) }
        ubicacionesGranRex.forEach { save(it) }
    }


    override fun afterPropertiesSet() {
        limpiarEntradas()
        crearBandas()
        crearUbicaciones()
        crearInstalaciones()
        crearUsuarios()
        crearShows()
        actualizarShows()
        usuariosCompranEntradas()
        actualizarShows()
        //crear400Shows() //testear el sharding.
    }
}