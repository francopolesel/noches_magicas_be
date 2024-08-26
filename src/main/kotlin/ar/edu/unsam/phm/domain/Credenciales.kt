package ar.edu.unsam.phm.domain

import java.math.BigDecimal

data class Credenciales(val username: String, val password: String)

data class Retorno(val id:Long,val administrador:Boolean)

data class EntradasAComprar(val id:String,val ubicacion: TipoUbicacion,val precio:BigDecimal)

