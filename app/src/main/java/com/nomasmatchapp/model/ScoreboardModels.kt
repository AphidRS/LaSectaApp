package com.nomasmatchapp.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ScoreboardModels(

    // La clave en el JSON es "posicion"
    @SerialName("posicion") val position: String,

    // La clave en el JSON es "nombre"
    @SerialName("nombre") val teamName: String,

    // La clave en el JSON es "url_img"
    @SerialName("url_img") val shieldUrl: String,

    // La clave en el JSON es "puntos"
    @SerialName("puntos") val points: String,

    // La clave en el JSON es "jugados"
    @SerialName("jugados") val played: String,

    // La clave en el JSON es "ganados"
    @SerialName("ganados") val won: String,

    // La clave en el JSON es "empatados"
    @SerialName("empatados") val drawn: String,

    // La clave en el JSON es "perdidos"
    @SerialName("perdidos") val lost: String,

    // La clave en el JSON es "goles_a_favor"
    @SerialName("goles_a_favor") val goalsFor: String,

    // La clave en el JSON es "goles_en_contra"
    @SerialName("goles_en_contra") val goalsAgainst: String,

    // La clave en el JSON es "puntos_sancion"
    @SerialName("puntos_sancion") val sanctionPoints: String

)
