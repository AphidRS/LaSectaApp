package com.lasectaapp.model
import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ScorersModels(
    @SerialName("codigo_jugador")
    val playerCode: String,

    @SerialName("foto")
    val photoUrl: String,

    @SerialName("jugador")
    val playerName: String,

    @SerialName("escudo_equipo")
    val teamShieldUrl: String,

    @SerialName("nombre_equipo")
    val teamName: String,

    @SerialName("codigo_equipo")
    val teamCode: String,

    @SerialName("partidos_jugados")
    val gamesPlayed: String,

    @SerialName("goles")
    val goals: String,

    @SerialName("goles_penalti")
    val penaltyGoals: String,

    @SerialName("goles_por_partidos")
    val goalsPerGame: String
)
