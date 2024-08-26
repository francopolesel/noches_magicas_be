package ar.edu.unsam.phm.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

class Funcion(
    var fecha: LocalDateTime,
    var tipoDeEntrada: Set<CapacidadUbicacion>,

    ) {
    var entradasVendidas: MutableList<RegistroEntradas> = mutableListOf()

    @JsonProperty
    var id: String = UUID.randomUUID().toString()

    var finalizo = finalizo()
    var soldOut = false

    init {
        tipoDeEntrada.forEach { entradasVendidas.add(RegistroEntradas(it.tipoUbicacion)) }
    }

    fun entradasTotales() = tipoDeEntrada.sumOf { it.capacidad }

    fun entradasVendidasTotales() = entradasVendidas.sumOf { it.entradasVendidas }

    fun entradasDisponiblesTotales() = entradasTotales() - entradasVendidasTotales()

    fun ubicaciones() = tipoDeEntrada.map { it.tipoUbicacion }

    fun soldOut(): Boolean = entradasDisponiblesTotales() == 0

    fun entradasDisponiblesPorUbicacion(tipoUbicacion: TipoUbicacion): Int {
        val valorTotales = tipoDeEntrada.find { it.tipoUbicacion == tipoUbicacion }
        val valorVendidos = entradasVendidas.find { it.tipoDeEntrada == tipoUbicacion }
        return if (valorTotales != null && valorVendidos != null) {
            valorTotales.capacidad - valorVendidos.entradasVendidas
        } else {
            throw Businessexception("no existe $tipoUbicacion")
        }
    }

    fun validarCompraDeEntrada(tipoUbicacion: TipoUbicacion) {
        if (entradasDisponiblesPorUbicacion(tipoUbicacion) > 0) {
            val entradaQueQuiero = entradasVendidas.find { it.tipoDeEntrada == tipoUbicacion }
            if (entradaQueQuiero != null) entradaQueQuiero.entradasVendidas++
        } else {
            throw Businessexception("no hay entradas disponibles para esa ubicacion $tipoUbicacion")
        }
    }

    fun venderEntrada(entrada: Entrada, usuario: UsuarioComun, precio: Double) {
        validarCompraDeEntrada(entrada.tipoUbicacion)
        usuario.comprarEntrada(entrada, precio)
    }

    fun perteneceAShow(show: Show): Boolean =
        show.funciones.contains(this)

    fun finalizo(): Boolean = fecha.isBefore(LocalDateTime.now())

    fun fueRealizado() = fecha.isBefore(LocalDateTime.now())

    fun simularSoldOut() {
        tipoDeEntrada.forEach { tipoEntrada ->
            entradasVendidas.find { it.tipoDeEntrada == tipoEntrada.tipoUbicacion }?.entradasVendidas =
                tipoEntrada.capacidad
        }
        soldOut = true
    }
}

class FuncionBuilder{
    var fecha = LocalDateTime.now()
    var tipoDeEntrada= setOf<CapacidadUbicacion>()
    fun fecha(fecha: LocalDateTime): FuncionBuilder {
        this.fecha = fecha
        return this
    }

    fun tipoDeEntrada(tipoDeEntrada: Set<CapacidadUbicacion>): FuncionBuilder {
        this.tipoDeEntrada = tipoDeEntrada
        return this
    }

    fun build(): Funcion {
        return Funcion(fecha, tipoDeEntrada)
    }
}

@Entity
data class RegistroEntradas(
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val tipoDeEntrada: TipoUbicacion,
) {
    var entradasVendidas: Int = 0

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}