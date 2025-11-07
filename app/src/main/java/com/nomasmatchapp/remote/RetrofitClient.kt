package com.nomasmatchapp.remote

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient

object RetrofitClient {

    private val okHttpClient = OkHttpClient.Builder().build()

    // Retrofit necesita una URL base para construirse, aunque no la usemos.
    // Ponemos una de relleno, ya que la URL real se pasará en cada llamada.
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://placeholder.com/") // URL de relleno
        .client(okHttpClient)
        // Este conversor es VITAL. Permite que Retrofit devuelva un String.
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    // Esta línea crea el objeto 'apiService' que tu Repository usa.
    // Si 'ApiService.kt' no existe, esta línea también daría error.
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
