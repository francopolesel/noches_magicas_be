package ar.edu.unsam.phm.bootstrap

import ar.edu.unsam.phm.NochesMagicasApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

class ServletInitializer : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(NochesMagicasApplication::class.java)
    }
}