package ar.edu.unsam.phm.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Banda(val nombre: String, var imagen: String, var costo: Double) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun cambiarCosto(nuevoCosto: Double) {
        costo = nuevoCosto
    }

    fun cambiarImagen(nuevaImagen: String) {
        imagen = nuevaImagen
    }
}

class BandaBuilder {

    var nombre: String = ""
    var imagen: String = ""
    var costo: Double = 0.0

    fun nombre(nombre: String): BandaBuilder {
        this.nombre = nombre
        return this
    }

    fun imagen(imagen: String): BandaBuilder {
        this.imagen = imagen
        return this
    }

    fun costo(costo: Double): BandaBuilder {
        this.costo = costo
        return this
    }

    fun build() = Banda(nombre,imagen,costo)

}
