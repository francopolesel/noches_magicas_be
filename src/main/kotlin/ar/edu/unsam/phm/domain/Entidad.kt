package ar.edu.unsam.phm.domain

interface Entidad {
    var id: Int
    fun esNueva(): Boolean = id == 0
}