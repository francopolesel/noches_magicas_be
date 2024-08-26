package ar.edu.unsam.phm.repository

import ar.edu.unsam.phm.domain.ClickData
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ClickRepository : MongoRepository<ClickData, String>{

    @Query(value = "{'idShow': ?0}", count = true)
    fun contarEventosPorIdShowEspecifico(idShow: String?): Int
}