package com.nomasmatchapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomasmatchapp.URLManager
import com.nomasmatchapp.data.MatchDetailsRepository
import com.nomasmatchapp.model.MatchDetails
import kotlinx.coroutines.launch

class MatchDetailsViewModel : ViewModel() {

    private val repository = MatchDetailsRepository()

    private val _matchDetails = MutableLiveData<MatchDetails?>()
    val matchDetails: LiveData<MatchDetails?> = _matchDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadMatchDetails(publicUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
// 1. Usamos el método correcto que devuelve un objeto Pair.
                val urlParts: Pair<String, String> = URLManager.parseActaUrl(publicUrl)

// 2. Ahora sí podemos acceder a .first y .second.
                val acta = urlParts.first
                val competitionParams = urlParts.second

                if (acta.isNotEmpty()) {
                    // 1. Llamamos a la función con los dos parámetros correctos.
                    // 2. 'details' será de tipo 'MatchDetails?' porque la función devuelve un tipo anulable.
                    val result: Result<MatchDetails> = repository.fetchMatchDetails(
                        acta = acta,
                        competitionParams = competitionParams
                    )

                    result.onSuccess { details ->
                        // Si la llamada fue exitosa, 'details' es el objeto MatchDetails.
                        // Lo publicamos en el LiveData.
                        _matchDetails.postValue(details)
                    }.onFailure { exception ->
                        // Si la llamada falló, 'exception' contiene el error.
                        // Lo publicamos en el LiveData de error.
                        Log.e("MatchDetailsVM", "El repositorio devolvió un error", exception)
                        _error.postValue("Error al obtener detalles: ${exception.message}")
                        _matchDetails.postValue(null) // Opcional: limpiar datos antiguos
                    }

                } else {
                    // Si la URL no es válida, lanzamos una excepción como antes.
                    throw IllegalArgumentException("URL de acta no válida: $publicUrl")
                }


            } catch (e: Exception) {
                Log.e("MatchDetailsVM", "Error al cargar detalles del partido", e)
                _error.postValue("Error al cargar los detalles: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
