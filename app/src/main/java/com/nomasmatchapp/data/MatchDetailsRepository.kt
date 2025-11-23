package com.nomasmatchapp.data

import android.util.Log

import com.nomasmatchapp.model.Goal
import com.nomasmatchapp.model.MatchDetails
import com.nomasmatchapp.model.Player
import com.nomasmatchapp.model.Referee
import com.nomasmatchapp.model.MatchModel
import com.nomasmatchapp.remote.ApiService
import com.nomasmatchapp.remote.RetrofitClient
import kotlinx.serialization.json.Json

class MatchDetailsRepository {

    private val apiService: ApiService = RetrofitClient.apiService
    private val jsonParser = Json { ignoreUnknownKeys = true }

    suspend fun fetchMatchDetails(acta: String, competitionParams: String): Result<MatchDetails> {
        val fullUrl = "https://www.rffm.es/api/acta_partido?codacta=$acta$competitionParams"
        Log.d("MatchDetailsRepo", "Requesting data from URL: $fullUrl")

        return try {
            val rawResponse: String = apiService.getPageContentAsString(fullUrl)
            val finalDetails = parseAndMapResponse(rawResponse)
            Result.success(finalDetails)
        } catch (e: Exception) {
            Log.e("MatchDetailsRepo", "Error al obtener o parsear los detalles: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun parseAndMapResponse(rawResponse: String): MatchDetails {
        val startMarker = "\"game\":"
        val startIndex = rawResponse.indexOf(startMarker)
        if (startIndex == -1) {
            throw IllegalStateException("El JSON no comienza con el marcador esperado '\"game\":'.")
        }

        val endMarker = ",\"seasonName\""
        val endIndex = rawResponse.indexOf(endMarker, startIndex)
        if (endIndex == -1) {
            throw IllegalStateException("No se encontró un marcador de finalización '$endMarker' válido para el JSON.")
        }

        val jsonString = rawResponse.substring(startIndex + startMarker.length, endIndex)
        Log.d("MatchDetailsRepo", "JSON extraído para parsear: $jsonString")

        val matchModel: MatchModel = jsonParser.decodeFromString(jsonString)
        return mapModelToDetails(matchModel)
    }
    private fun mapModelToDetails(model: MatchModel): MatchDetails {
        val localLineup = model.jugadoresEquipoLocal
            .filter { it.titular == "1" }
            .map { Player(dorsal = it.dorsal, nombre = it.nombreJugador) }

        val visitorLineup = model.jugadoresEquipoVisitante
            .filter { it.titular == "1" }
            .map { Player(dorsal = it.dorsal, nombre = it.nombreJugador) }

        val allGoals = (model.golesEquipoLocal + model.golesEquipoVisitante)
            .sortedBy { it.minuto.toIntOrNull() ?: 0 }
            .map {
                Goal(minuto = it.minuto, jugador = it.nombreJugador, resultado = "")
            }

        val referees = model.arbitrosPartido.map {
            Referee(rol = it.tipoArbitro, nombre = it.nombreArbitro)
        }

        return MatchDetails(
            jornada = model.jornada,
            fecha = model.fecha,
            resultado = "${model.golesLocal} - ${model.golesVisitante}",
            equipo_local = model.equipoLocal,
            equipo_visitante = model.equipoVisitante,
            escudo_local = model.escudoLocal,
            escudo_visitante = model.escudoVisitante,
            alineacion_local = localLineup,
            alineacion_visitante = visitorLineup,
            goles = allGoals,
            arbitros = referees
        )
    }
}
