package com.lasectaapp.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un único partido dentro de una jornada.
 *
 * Los nombres de las variables en Kotlin (camelCase) son más limpios que los del JSON (snake_case).
 * La anotación @SerializedName le dice a la librería GSON cómo mapear cada campo.
 * Por ejemplo, el campo "equipo_local" del JSON se guardará en la variable "nombreEquipoLocal".
 */
data class Match(
    @SerializedName("codacta")
    val codActa: String,

    @SerializedName("codigo_equipo_local")
    val codigoEquipoLocal: String,

    @SerializedName("escudo_equipo_local")
    val escudoEquipoLocal: String,

    @SerializedName("equipo_local")
    val nombreEquipoLocal: String,

    @SerializedName("goles_casa")
    val golesCasa: String?, // Puede ser nulo si el partido no se ha jugado

    @SerializedName("codigo_equipo_visitante")
    val codigoEquipoVisitante: String,

    @SerializedName("escudo_equipo_visitante")
    val escudoEquipoVisitante: String,

    @SerializedName("equipo_visitante")
    val nombreEquipoVisitante: String,

    @SerializedName("goles_visitante")
    val golesVisitante: String?, // Puede ser nulo si el partido no se ha jugado

    @SerializedName("campo")
    val campo: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("hora")
    val hora: String
)

/**
 * Representa una jornada completa, que se identifica por su número
 * y contiene una lista de todos los partidos (`Match`) de esa jornada.
 */

data class Round(
    @SerializedName("codjornada")
    val codJornada: String,

    @SerializedName("jornada")
    val jornada: String,

    @SerializedName("equipos") // En el JSON, la lista de partidos se llama "equipos"
    val partidos: List<Match>
)
