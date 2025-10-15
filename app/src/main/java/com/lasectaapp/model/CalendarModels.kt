package com.lasectaapp.model


import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representa un único partido dentro de una jornada.
 *
 * Los nombres de las variables en Kotlin (camelCase) son más limpios que los del JSON (snake_case).
 * La anotación @SerializedName le dice a la librería GSON cómo mapear cada campo.
 * Por ejemplo, el campo "equipo_local" del JSON se guardará en la variable "nombreEquipoLocal".
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Match(
    @SerialName("codacta")
    val codacta: String,
    @SerialName("codigo_equipo_local")
    val codigo_equipo_local: String,
    @SerialName("escudo_equipo_local")
    val escudo_equipo_local: String,
    @SerialName("equipo_local")
    val equipo_local: String,
    @SerialName("goles_casa")
    val goles_casa: String,
    @SerialName("codigo_equipo_visitante")
    val codigo_equipo_visitante: String,
    @SerialName("escudo_equipo_visitante")
    val escudo_equipo_visitante: String,
    @SerialName("equipo_visitante")
    val equipo_visitante: String,
    @SerialName("goles_visitante")
    val goles_visitante: String,
    @SerialName("codigo_campo")
    val codigo_campo: String,
    @SerialName("campo")
    val campo: String,
    @SerialName("fecha")
    val fecha: String,
    @SerialName("hora")
    val hora: String
)

/**
 * Representa una jornada completa, que se identifica por su número
 * y contiene una lista de todos los partidos (`Match`) de esa jornada.
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Round(
    @SerialName("codjornada")
    val codJornada: String,

    @SerialName("jornada")
    val jornada: Int,

    @SerialName("equipos") // Le dice al parser que busque "equipos" en el JSON
    val partidos: List<Match>,

    @Transient // Esta anotación evita que el parser intente leerla del JSON
    val rawJsonForDebug: String? = ""

)
