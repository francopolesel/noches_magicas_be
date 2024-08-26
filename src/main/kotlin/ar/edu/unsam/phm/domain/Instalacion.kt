package ar.edu.unsam.phm.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


enum class TipoUbicacion(val costo: Double) {
    PLATEA_ALTA(10000.0),
    CAMPO(15000.0),
    PALCO(20000.0),
    PULLMAN(10000.0),
    PLATEA_BAJA(15000.0)
}

@Entity
@Inheritance(strategy = InheritanceType.JOINED) //Elegimos JOIN para que no haya muchos valores nulos
abstract class Instalacion(
    val nombre: String, val coordenadaX: Double, val coordenadaY: Double,
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE]) //MERGE PORQUE QUIERO QUE SOLO SE ME SINCRONICE CUANDO GUARDO LA ENTIDAD PRINCIPAL
    @JoinColumn(name = "fk_instalacion_id")
    val capacidad: Set<CapacidadUbicacion>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //LA BD ASIGNA AUTOMATICAMENTE VALORES NUEVOS A LA PK CUANDO SE INSERTA UNA NUEVA FILA
    var id: Long = 0

    fun costoPorUbicacion(tipoUbicacion: TipoUbicacion): Double = tipoUbicacion.costo

    abstract fun costoFijo(): Double

    fun plazasTotales(): Int {
        return capacidad.sumOf { it.capacidad }
    }
}

@Entity
class Estadio(
    nombre: String, coordenadaX: Double, coordenadaY: Double, capacidad: Set<CapacidadUbicacion>,
    @JsonIgnore
    val costoFijo: Double,
) : Instalacion(nombre, coordenadaX, coordenadaY, capacidad) {

    override fun costoFijo(): Double = costoFijo
}

@Entity
class Teatro(
    nombre: String, coordenadaX: Double, coordenadaY: Double, capacidad: Set<CapacidadUbicacion>,
    @JsonIgnore
    val buenaAcustica: Boolean,
) : Instalacion(nombre, coordenadaX, coordenadaY, capacidad) {

    override fun costoFijo(): Double = if (buenaAcustica) 150000.0 else 100000.0
}

class InstalacionBuilder {
    var nombre: String = ""
    var coordenadaX: Double = 0.0
    var coordenadaY: Double = 0.0
    var capacidad: Set<CapacidadUbicacion> = setOf<CapacidadUbicacion>()
    var costoFijo:Double = 0.0
    var buenaAcustica:Boolean = false
    fun nombre(nombre: String): InstalacionBuilder {
        this.nombre = nombre
        return this
    }

    fun coordenadaX(coordenadaX: Double): InstalacionBuilder {
        this.coordenadaX = coordenadaX
        return this
    }

    fun coordenadaY(coordenadaY: Double): InstalacionBuilder {
        this.coordenadaY = coordenadaY
        return this
    }

    fun capacidad(capacidad: Set<CapacidadUbicacion>): InstalacionBuilder {
        this.capacidad = capacidad
        return this
    }
    fun costoFijo(costoFijo: Double): InstalacionBuilder {
        this.costoFijo = costoFijo
        return this
    }
    fun buenaAcustica(buenaAcustica: Boolean): InstalacionBuilder {
        this.buenaAcustica = buenaAcustica
        return this
    }

    fun buildEstadio(): Estadio {
        return Estadio(nombre, coordenadaX, coordenadaY, capacidad, costoFijo)
    }


    fun buildTeatro(): Teatro {
        return Teatro(nombre, coordenadaX, coordenadaY, capacidad, buenaAcustica)
    }
}

@Entity
data class CapacidadUbicacion(
    @Enumerated(EnumType.STRING) //INDICO COMO QUIERO PERSISTIR ESTE DATO
    @Column(length = 20)
    val tipoUbicacion: TipoUbicacion,
    val capacidad: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}
