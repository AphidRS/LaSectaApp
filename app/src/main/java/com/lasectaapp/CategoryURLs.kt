// Archivo: app/src/main/java/com/lasectaapp/CategoryURLs.kt
package com.lasectaapp
import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CategoryURLs(
    val calendarioUrl: String,
    val clasificacionUrl: String,
    val goleadoresUrl: String
)
    