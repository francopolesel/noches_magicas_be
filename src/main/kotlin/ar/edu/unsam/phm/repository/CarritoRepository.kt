package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.CarritoDeCompras
import org.springframework.data.repository.CrudRepository

interface CarritoRepository:CrudRepository<CarritoDeCompras,String> {

}