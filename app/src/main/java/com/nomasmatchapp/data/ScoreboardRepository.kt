package com.nomasmatchapp.data

import com.nomasmatchapp.model.ScoreboardModels
import com.nomasmatchapp.remote.RetrofitClient
import kotlinx.serialization.json.Json

class ScoreboardRepository {

    private val apiService = RetrofitClient.apiService
    private val jsonParser = Json { ignoreUnknownKeys = true }
    suspend fun fetchScoreboardFromUrl(url: String): Result<List<ScoreboardModels>> {
        return try {

            val rawResponse: String = apiService.getPageContentAsString(url)

            val startMarker = "\"clasificacion\":"
            val startIndex = rawResponse.indexOf(startMarker)
            if (startIndex == -1) {
                throw IllegalStateException("La clave '\"clasificacion\":' no se encontró en la respuesta.")
            }

            val endMarker = ",\"promociones\""
            val endIndex = rawResponse.indexOf(endMarker)
            if (endIndex == -1) {
                throw IllegalStateException("El marcador de final ',\"currentRound\"' no se encontró.")
            }

            val jsonArrayString = rawResponse.substring(startIndex + startMarker.length, endIndex)
            val parsedRounds: List<ScoreboardModels> = jsonParser.decodeFromString(jsonArrayString)
            Result.success(parsedRounds)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
