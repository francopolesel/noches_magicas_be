package ar.edu.unsam.phm.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Id as IdNeo4j
import org.springframework.data.neo4j.core.schema.GeneratedValue as GeneratedValueNeo4j
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@Entity
@Node
class Entrada(
    val numero: Int,
    val tipoUbicacion: TipoUbicacion,
    @JsonIgnore
    val idFuncion: String,
    @JsonIgnore
    //@TargetNode
    val idShow: String

) {
    @Id
    @IdNeo4j
    @GeneratedValueNeo4j
    var id:Long= 0

}