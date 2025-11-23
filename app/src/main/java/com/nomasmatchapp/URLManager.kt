package com.nomasmatchapp

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Spinner

@SuppressLint("StaticFieldLeak")
object URLManager {
    private const val BASE_URL_ACTA = "https://www.rffm.es/acta_partido/"
    private lateinit var spinner: Spinner
    var currentCategory: CategoryURLs = CategoryURLs(
        calendarioUrl = "https://www.rffm.es/competicion/calendario?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
        clasificacionUrl = "https://www.rffm.es/competicion/clasificaciones?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
        goleadoresUrl = "https://www.rffm.es/competicion/goleadores?temporada=21&competicion=24037796&grupo=24037872&tipojuego=2"
    )
    fun initialize(spinner: Spinner) {
        this.spinner = spinner
    }
    fun getCalendarUrl(): String = currentCategory.calendarioUrl
    fun getClassificationUrl(): String = currentCategory.clasificacionUrl
    fun getScorersUrl(): String = currentCategory.goleadoresUrl
    fun getActaUrl(acta: String): String {
        if (!::spinner.isInitialized) {
            // Si el spinner no se ha inicializado, devuelve una URL por defecto o lanza un error
            Log.e("URLManager", "Spinner no inicializado!")
            return "" // O una URL por defecto
        }

        // Comprueba la posición seleccionada en el momento de la llamada
        val competitionParams = when (spinner.selectedItemPosition) {
            0 -> "?temporada=21&competicion=24037796&grupo=24037872" // Primera opción
            1 -> "?temporada=21&competicion=24037796&grupo=24037828" // Segunda opción
            else -> "" // Opción por defecto o para otras selecciones
        }

        return "$BASE_URL_ACTA$acta$competitionParams"
    }

    /**
     * Descompone una URL pública completa del acta en sus dos partes: el código del acta y los parámetros.
     * Reutiliza la lógica de getActaUrl para asegurar que los parámetros de competición son los correctos.
     * @param fullUrl La URL completa, por ejemplo "https://www.rffm.es/acta_partido/5500320?temporada=21&..."
     * @return Un Pair con el código del acta ("5500320") y los parámetros de competición ("?temporada=21&...").
     */
    fun parseActaUrl(fullUrl: String): Pair<String, String> {
        // 1. Extraemos el código del acta de la URL que nos llega.
        val actaCode = fullUrl.substringAfter(BASE_URL_ACTA).substringBefore('?')
        if (actaCode.isBlank()) {
            Log.e("URLManager", "No se pudo extraer el código del acta de: $fullUrl")
            return Pair("", "")
        }

        // 2. Reutilizamos la lógica existente para obtener los parámetros correctos según el Spinner.
        // Llamamos a getActaUrl() para que nos dé la URL completa y correcta.
        val correctFullUrl = getActaUrl(actaCode)
        if (correctFullUrl.isBlank()) {
            Log.e("URLManager", "getActaUrl devolvió una URL vacía para el código: $actaCode")
            return Pair("", "")
        }

        // 3. Extraemos los parámetros de la URL recién generada y correcta.
        val competitionParams = correctFullUrl.substringAfter('?', "")
        if (competitionParams.isBlank()) {
            Log.w("URLManager", "No se encontraron parámetros de competición para el acta: $actaCode")
            return Pair(actaCode, "") // Devolvemos el acta pero sin parámetros
        }

        // 4. Devolvemos el Pair con el código del acta y los parámetros correctos.
        return Pair(actaCode, "?$competitionParams")
    }
}
