package ar.edu.unsam.phm.domain

import jakarta.persistence.*
import java.time.LocalDate
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Id as IdNeo4j
@Entity
@Node
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Usuario(
    var nombre: String,
    var apellido: String,
    var fechaNacimiento: LocalDate,
    var username: String,
    var password: String,
) {

    @Id
    @IdNeo4j
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    @Relationship(type = "AMIGO_DE", direction = Relationship.Direction.OUTGOING)
    var amigos: MutableSet<Usuario> = HashSet()
    var imagen: String = ""
    var saldo: Double = 999999.0

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE])
    @JoinColumn(name = "fk_usuario_id")
    @Relationship(type = "ASISTIO_A", direction = Relationship.Direction.OUTGOING)
    var entradas: MutableSet<Entrada> = mutableSetOf()

    abstract fun esAdministrador(): Boolean

    fun amigoComento(show: Show): List<Long> = (show.usuariosQueComentaron().intersect(amigos.map { it.id }.toSet())).toList()

    fun agregarAmigo(usuario: Usuario) {
        amigos.add(usuario)
    }

    fun actualizarSaldo(saldo: Double) {
        this.saldo -= saldo
    }

    fun eliminarAmigo(usuario: Usuario) {
        amigos.remove(usuario)
    }

    fun cambiarImagen(nuevaImagen: String) {
        imagen = nuevaImagen
    }

    fun nombreCompleto(): String = "$nombre $apellido"


    fun agregarSaldo(saldo: Double) {
        this.saldo += saldo
    }

    fun esAmigo(usuarioAmigo: Usuario) = amigos.contains(usuarioAmigo)

    fun asistoAUnShow(show: Show): Boolean {
        return entradas.any { it.idShow == show.id }
    }

    fun amigosQueAsisten(show: Show): List<Usuario> = amigos.filter { it.asistoAUnShow(show) }

    fun validarSaldo(precio: Double) = if (precio>saldo) throw Businessexception("No te alcanza el saldo") else actualizarSaldo(precio)
}

@Entity
class UsuarioAdministrador(
    nombre: String,
    apellido: String,
    fechaNacimiento: LocalDate,
    username: String,
    password: String,
) : Usuario(nombre, apellido, fechaNacimiento, username, password) {

    override fun esAdministrador(): Boolean = true
    //override fun asistoAUnShow(show: Show): Boolean = false


    fun ingresosTotalShow(funciones: MutableSet<Funcion>, show: Show): Double {
        return funciones.sumOf { ingresosTotalFuncion(it, show) }
    }

    fun ingresosTotalFuncion(funcion: Funcion, show: Show): Double {
        val tiposEntradasVendidas = funcion.entradasVendidas
        val ingresosEntradas = funcion.entradasVendidas.sumOf { it ->
            ingresosDelTipoDeEntradaFuncion(
                it.entradasVendidas,
                show.precioDeEntrada(it.tipoDeEntrada)
            )
        }
        return ingresosEntradas
    }

    fun totalEntradasVendidasShow(funciones: Set<Funcion>): Int = funciones.sumOf { it.entradasVendidasTotales() }

    fun totalEntradasVendidasPorTipo(funciones: Set<Funcion>): List<Pair<TipoUbicacion, Int>> {
        val diccionarioCombinado = funciones.flatMap { it.entradasVendidas }
        val mapaSuma =
            diccionarioCombinado.groupBy({ it.tipoDeEntrada }, { it.entradasVendidas }).mapValues { it.value.sum() }
        return mapaSuma.map { it.key to it.value }
    }

    fun puedoCrearFuncion(
        funciones: Set<Funcion>,
        suscriptores: Int,
        entradaMasBarata: Double,
        costoShow: Double
    ): Boolean {
        val todasSoldout = funciones.all { it.soldOut() }

        return todasSoldout && cubreCostos(suscriptores, entradaMasBarata, costoShow) || funciones.isEmpty()
    }

    fun cubreCostos(cantidadSuscriptores: Int, precioEntradaMasBarata: Double, costoShow: Double): Boolean =
        cantidadSuscriptores * precioEntradaMasBarata > costoShow

    private fun ingresosDelTipoDeEntradaFuncion(entradasVendidasFuncion: Int, precioEntrada: Double): Double =
        entradasVendidasFuncion * precioEntrada

    fun cambiarEstadoDeUnShow(show: Show, nuevoEstadoEnum: EstadoDeShow) {
        show.cambiarEstado(nuevoEstadoEnum)
    }

    fun cambiarNombreDeUnShow(show: Show, nuevoNombre: String) {
        show.evento = nuevoNombre
    }
}

@Entity
class UsuarioComun(
    nombre: String,
    apellido: String,
    fechaNacimiento: LocalDate,
    username: String,
    password: String,
) : Usuario(nombre, apellido, fechaNacimiento, username, password) {

    fun comprarEntrada(entrada: Entrada, precio: Double) {
        validarSaldo(precio)
        entradas.add(entrada)
    }

    override fun esAdministrador(): Boolean = false

}

class UsuarioBuilder() {

    var nombre: String = ""
    var apellido: String = ""
    var fechaNacimiento: LocalDate = LocalDate.now()
    var username: String = ""
    var password: String = ""
    var imagen: String = ""
    var saldo: Double = 999999.0
    var amigos: MutableSet<Usuario> = HashSet()
    var entradas: MutableSet<Entrada> = mutableSetOf()
    fun nombre(nombre: String): UsuarioBuilder {
        this.nombre = nombre
        return this
    }

    fun apellido(apellido: String): UsuarioBuilder {
        this.apellido = apellido
        return this
    }

    fun fechaNacimiento(fechaNacimiento: LocalDate): UsuarioBuilder {
        this.fechaNacimiento = fechaNacimiento
        return this
    }

    fun username(username: String): UsuarioBuilder {
        this.username = username
        return this
    }

    fun password(password: String): UsuarioBuilder {
        this.password = password
        return this
    }

    fun imagen(imagen: String): UsuarioBuilder {
        this.imagen = imagen
        return this
    }

    fun saldo(saldo: Double): UsuarioBuilder {
        this.saldo = saldo
        return this
    }

    fun amigos(amigos: MutableSet<Usuario>): UsuarioBuilder {
        this.amigos = amigos
        return this
    }

    fun entradas(entradas: MutableSet<Entrada>): UsuarioBuilder {
        this.entradas = entradas
        return this
    }

    fun buildAdministrador(): UsuarioAdministrador {
        val usuarioADM = UsuarioAdministrador(this.nombre, this.apellido, this.fechaNacimiento, this.username, this.password)
        usuarioADM .imagen = this.imagen
        usuarioADM .saldo = this.saldo
        usuarioADM .amigos = this.amigos
        usuarioADM .entradas = this.entradas
        return usuarioADM
    }

    fun buildComun(): UsuarioComun {
        val usuarioCOM = UsuarioComun(this.nombre, this.apellido, this.fechaNacimiento, this.username, this.password)
        usuarioCOM .imagen = this.imagen
        usuarioCOM .saldo = this.saldo
        usuarioCOM .amigos = this.amigos
        usuarioCOM .entradas = this.entradas
        return usuarioCOM
    }
}


