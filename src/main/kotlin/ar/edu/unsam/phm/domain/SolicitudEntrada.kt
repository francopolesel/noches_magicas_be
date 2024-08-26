package ar.edu.unsam.phm.domain

data class SolicitudEntrada (val idEntrada: Int)

data class SolicitudEntradaCarrito(val id:String, val ubicacion: TipoUbicacion, val cantidad: Int)