package ar.edu.unsam.phm.domain

import ar.edu.unsam.phm.DTO.CardShowMongoDTO

class Filtro(
    var artista: String = "",
    var locacion: String = "",
    var conAmigos: Boolean = false,
) {
    fun filterData(shows: List<CardShowMongoDTO>): List<CardShowMongoDTO> {
        val artistaUpper = artista.uppercase()
        val locacionUpper = locacion.uppercase()

        val filteredShows = if (conAmigos) {
            shows.filter {
                it.banda.uppercase().contains(artistaUpper) && it.nombreInstalacion.uppercase()
                    .contains(locacionUpper) &&
                        it.amigosQueAsisten.isNotEmpty()

            }
        } else {
            shows.filter {
                it.banda.uppercase().contains(artistaUpper) && it.nombreInstalacion.uppercase()
                    .contains(locacionUpper)
            }
        }
        return filteredShows
    }
}