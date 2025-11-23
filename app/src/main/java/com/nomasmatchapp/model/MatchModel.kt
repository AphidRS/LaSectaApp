package com.nomasmatchapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- CLASE PRINCIPAL QUE REPRESENTA TODO EL JSON ---
@Serializable
data class MatchModel(
    val jornada: String,
    val fecha: String,
    val hora: String,

    @SerialName("equipo_local")
    val equipoLocal: String,

    @SerialName("escudo_local")
    val escudoLocal: String,

    @SerialName("goles_local")
    val golesLocal: String,

    @SerialName("equipo_visitante")
    val equipoVisitante: String,

    @SerialName("escudo_visitante")
    val escudoVisitante: String,

    @SerialName("goles_visitante")
    val golesVisitante: String,

    @SerialName("goles_equipo_local")
    val golesEquipoLocal: List<ActaGoal>,

    @SerialName("goles_equipo_visitante")
    val golesEquipoVisitante: List<ActaGoal>,

    @SerialName("jugadores_equipo_local")
    val jugadoresEquipoLocal: List<ActaPlayer>,

    @SerialName("jugadores_equipo_visitante")
    val jugadoresEquipoVisitante: List<ActaPlayer>,

    @SerialName("arbitros_partido")
    val arbitrosPartido: List<ActaReferee>
)

// --- CLASES ANIDADAS ---

@Serializable
data class ActaGoal(
    @SerialName("nombre_jugador")
    val nombreJugador: String,
    val minuto: String
)

@Serializable
data class ActaPlayer(
    val dorsal: String,
    @SerialName("nombre_jugador")
    val nombreJugador: String,
    val titular: String // "1" si es titular, "0" si no
)

@Serializable
data class ActaReferee(
    @SerialName("tipo_arbitro")
    val tipoArbitro: String,
    @SerialName("nombre_arbitro")
    val nombreArbitro: String
)
