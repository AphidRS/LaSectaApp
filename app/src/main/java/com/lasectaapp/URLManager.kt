// Archivo: app/src/main/java/com/lasectaapp/URLManager.kt
package com.lasectaapp

object URLManager {
    // Almacenamos el objeto completo con las 3 URLs de la categoría actual.
    // Lo inicializamos con valores por defecto para "Benjamin A".
    var currentCategory: CategoryURLs = CategoryURLs(
        calendarioUrl = "https://www.rffm.es/competicion/calendario?idcompeticion=27814&idgrupo=29124&idjornada=1",
        clasificacionUrl = "https://www.rffm.es/competicion/clasificacion?idcompeticion=27814&idgrupo=29124",
        goleadoresUrl = "https://www.rffm.es/competicion/goleadores?idcompeticion=27814&idgrupo=29124"
    )
}
    