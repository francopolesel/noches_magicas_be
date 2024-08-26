package ar.edu.unsam.phm.domain


import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("CarritoDeCompras", timeToLive = 18000)
class CarritoDeCompras(val idUsuario: Long) {

    @Id
    var id: String = idUsuario.toString()

    var entradas: MutableList<Entrada> = mutableListOf()
}