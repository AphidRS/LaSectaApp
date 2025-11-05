package com.lasectaapp.data

import android.util.Log
import com.lasectaapp.model.ScoreboardModels
import com.lasectaapp.remote.RetrofitClient
import kotlinx.serialization.json.Json

class ScoreboardRepository {

    private val apiService = RetrofitClient.apiService
    private val jsonParser = Json { ignoreUnknownKeys = true }
    suspend fun fetchScoreboardFromUrl(url: String): Result<List<ScoreboardModels>> {
        return try {
            // 1. LLAMADA DE RED
            val rawResponse: String = apiService.getPageContentAsString(url)

            // 2. BUSCAR EL INICIO DEL ARRAY
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
            // Log de depuración para verificar el JSON extraído
            //Log.d("RoundsRepository", "JSON extraído: $jsonArrayString")

            // 5. PARSEO DEL JSON
            val parsedRounds: List<ScoreboardModels> = jsonParser.decodeFromString(jsonArrayString)

            // 6. ÉXITO (enviamos la lista de depuración)
            Result.success(parsedRounds)

        } catch (e: Exception) {
            // ... El bloque catch permanece igual ...
            Log.e("ScoreBoard", "FALLO AL PARSEAR. Devolviendo datos de error para la UI.", e)

            return Result.failure(e)
        }
    }
}
