// Archivo: app/src/main/java/com/lasectaapp/URLManager.kt
package com.lasectaapp

object URLManager {
    // Almacenamos el objeto completo con las 3 URLs de la categoría actual.
    // Lo inicializamos con valores por defecto para "Benjamin A".
    var currentCategory: CategoryURLs = CategoryURLs(
        calendarioUrl = "https://www.rffm.es/competicion/calendario?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
        clasificacionUrl = "https://www.rffm.es/competicion/clasificaciones?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
        goleadoresUrl = "https://www.rffm.es/competicion/goleadores?temporada=21&competicion=24037796&grupo=24037872&tipojuego=2"
    )
    // A partir de la categoría actual, podemos exponer las URLs individuales
    fun getCalendarUrl(): String = currentCategory.calendarioUrl
    fun getClassificationUrl(): String = currentCategory.clasificacionUrl
    fun getScorersUrl(): String = currentCategory.goleadoresUrl

}
    