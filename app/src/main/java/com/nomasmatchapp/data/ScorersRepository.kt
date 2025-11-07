package com.nomasmatchapp.data

import com.nomasmatchapp.model.ScorersModels
import com.nomasmatchapp.remote.RetrofitClient
import kotlinx.serialization.json.Json

class ScorersRepository {

    private val apiService = RetrofitClient.apiService
    private val jsonParser = Json { ignoreUnknownKeys = true }
    private lateinit var startMarker: String
    suspend fun fetchScorersFromUrl(url: String): Result<List<ScorersModels>> {
        return try {

            val rawResponse: String = apiService.getPageContentAsString(url)

            if (url.equals("https://www.rffm.es/competicion/goleadores?temporada=21&competicion=24037796&grupo=24037872&tipojuego=2")) {
                startMarker = "38\",\"goles\":"
            } else {
                startMarker = "37\",\"goles\":"
            }
            val startIndex = rawResponse.indexOf(startMarker)
            if (startIndex == -1) {
                throw IllegalStateException("La clave '\"goles\":' no se encontró en la respuesta. Revisa el JSON.")
            }

            val endMarker = "},\"group\""
            val endIndex = rawResponse.indexOf(endMarker)
            if (endIndex == -1) {
                throw IllegalStateException("El marcador de final ',\"group\"' no se encontró después del inicio. Revisa el JSON.")
            }

            val jsonArrayString = rawResponse.substring(startIndex + startMarker.length, endIndex)
            val parsedScorers: List<ScorersModels> = jsonParser.decodeFromString(jsonArrayString)
            Result.success(parsedScorers)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
