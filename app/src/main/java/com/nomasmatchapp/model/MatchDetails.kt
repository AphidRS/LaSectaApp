package com.nomasmatchapp.model

data class MatchDetails(
    val jornada: String,
    val fecha: String,
    val resultado: String,
    val equipo_local: String,
    val equipo_visitante: String,
    val escudo_local: String,
    val escudo_visitante: String,
    val alineacion_local: List<Player>,
    val alineacion_visitante: List<Player>,
    val goles: List<Goal>,
    val arbitros: List<Referee>
)
    