package ar.edu.unsam.phm


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@SpringBootApplication
@Configuration
@EnableJpaRepositories(basePackages = ["ar.edu.unsam.phm.repository"])
@EnableNeo4jRepositories(basePackages = ["ar.edu.unsam.phm.neo4j"])
class NochesMagicasApplication

fun main(args: Array<String>) {
    runApplication<NochesMagicasApplication>(*args)
}
