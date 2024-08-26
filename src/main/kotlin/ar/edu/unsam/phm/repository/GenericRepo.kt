package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.CarritoDeCompras
import ar.edu.unsam.phm.domain.ElementoInexistenteException
import ar.edu.unsam.phm.domain.ElementoYaExisteException
import ar.edu.unsam.phm.domain.Entidad
import jakarta.transaction.Transactional
import org.hibernate.Hibernate
import org.springframework.stereotype.Component


@Component
class GenericRepo<T : Entidad> {
    val elementos: MutableList<T> = mutableListOf()
    var nextId = 1

    fun create(elemento: T) {
        validarElementoNuevo(elemento)
        asignarId(elemento)
        elementos.add(elemento)
    }

    private fun validarElementoNuevo(elemento: T) {
        if (!elemento.esNueva()) {
            throw ElementoYaExisteException(
                "No se puede crear el elemento con ID ${elemento.id}" +
                        ". Este ID ya existe en el repositorio"
            )
        }
    }

    private fun asignarId(elemento: T) {
        elemento.id = nextId++
    }

    fun delete(elemento: T) {
        val elementoExistente = getById(elemento.id)
        elementos.remove(elementoExistente)
    }

    fun update(elemento: T) {
        val elementoViejo = getById(elemento.id)
        reemplazarElemento(elementoViejo, elemento)
    }

    private fun reemplazarElemento(elementoViejo: T, nuevo: T) {
        val index = elementos.indexOf(elementoViejo)
        if (index != -1) {
            elementos[index] = nuevo
        }
    }

    fun getById(id: Int): T {
        return elementos.find { elemento -> elemento.id == id }
            ?: throw ElementoInexistenteException("No se encontró el elemento con ID $id")
    }

    fun allInstances() = elementos
}

//@Component
//class RepositorioCarrito : GenericRepo<CarritoDeCompras>() {
//
//    @Transactional
//    fun getCarritoByIdUsuario(idUsuario: Long): CarritoDeCompras {
//        val carrito = elementos.find { it.idUsuario == idUsuario }
//        if (carrito == null) {
//            val newCarrito = CarritoDeCompras(idUsuario)
//            create(newCarrito)
//            return newCarrito
//        } else {
//            carrito.entradas.forEach { Hibernate.initialize(it.idShow) } //SE FUERZA LA CARGA DE ESTOS DATOS PORQUE ES LAZY
//            return carrito
//        }
//    }
//}
