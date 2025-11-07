package com.nomasmatchapp.data

import com.nomasmatchapp.model.Round
import com.nomasmatchapp.remote.RetrofitClient
import kotlinx.serialization.json.Json

class RoundsRepository {

    private val apiService = RetrofitClient.apiService
    private val jsonParser = Json { ignoreUnknownKeys = true }
    suspend fun fetchRoundsFromUrl(url: String): Result<List<Round>> {
        return try {

            val rawResponse: String = apiService.getPageContentAsString(url)

            val startMarker = "\"rounds\":"
            val startIndex = rawResponse.indexOf(startMarker)
            if (startIndex == -1) {
                throw IllegalStateException("La clave '\"rounds\":' no se encontró en la respuesta.")
            }

            val endMarker = "},\"currentRound\""
            val endIndex = rawResponse.indexOf(endMarker)
            if (endIndex == -1) {
                throw IllegalStateException("El marcador de final ',\"currentRound\"' no se encontró.")
            }

            val jsonArrayString = rawResponse.substring(startIndex + startMarker.length, endIndex)
            val parsedRounds: List<Round> = jsonParser.decodeFromString(jsonArrayString)
            Result.success(parsedRounds)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
