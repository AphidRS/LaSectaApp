package com.lasectaapp.model


import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Round(
    @SerialName("codjornada")
    val codJornada: String,

    @SerialName("jornada")
    val jornada: Int,

    @SerialName("equipos")
    val partidos: List<Match>,

    @Transient
    val rawJsonForDebug: String? = ""

)
