package com.lasectaapp.data

import android.util.Log
import com.lasectaapp.model.Round
import com.lasectaapp.remote.RetrofitClient
import kotlinx.serialization.json.Json

class RoundsRepository {

    private val apiService = RetrofitClient.apiService
    private val jsonParser = Json { ignoreUnknownKeys = true }
    suspend fun fetchRoundsFromUrl(url: String): Result<List<Round>> {
        return try {
            // 1. LLAMADA DE RED
            val rawResponse: String = apiService.getPageContentAsString(url)

            // 2. BUSCAR EL INICIO DEL ARRAY
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
            // Log de depuración para verificar el JSON extraído
            //Log.d("RoundsRepository", "JSON extraído: $jsonArrayString")

            // 5. PARSEO DEL JSON
            val parsedRounds: List<Round> = jsonParser.decodeFromString(jsonArrayString)

            // 5.1 CREAR LISTA CON DATOS DE DEPURACIÓN
            val roundsForDebug = parsedRounds.map { round ->
                round.copy(rawJsonForDebug = "ÉXITO: " + jsonArrayString)
            }

            //Log.d("RoundsRepository", "Parseo exitoso. Número de jornadas: ${roundsForDebug.size}")

            // 6. ÉXITO (enviamos la lista de depuración)
            Result.success(roundsForDebug)

        } catch (e: Exception) {
            // ... El bloque catch permanece igual ...
            Log.e("RoundsRepository", "FALLO AL PARSEAR. Devolviendo datos de error para la UI.", e)

            return Result.failure(e)
        }
    }
}
