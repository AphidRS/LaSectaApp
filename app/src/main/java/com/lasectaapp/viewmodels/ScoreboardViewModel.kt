package com.lasectaapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasectaapp.data.ScoreboardRepository
import com.lasectaapp.model.ScoreboardModels
import kotlinx.coroutines.launch

class ScoreboardViewModel : ViewModel() {


    private val repository = ScoreboardRepository()

    // LiveData privado para los datos de la clasificación. Solo el ViewModel puede modificarlo.
    private val _standings = MutableLiveData<List<ScoreboardModels>>()
    // LiveData público y de solo lectura que el Fragment observará.
    val standings: LiveData<List<ScoreboardModels>> = _standings

    // LiveData para controlar la visibilidad del ProgressBar.
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para mostrar mensajes de error.
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Función que el Fragment llamará para iniciar la carga de datos.
     * Usa coroutines (viewModelScope) para realizar la llamada de red en un hilo secundario
     * sin bloquear la interfaz de usuario.
     */
    fun loadClassification(url: String) {
        viewModelScope.launch {
            _isLoading.value = true // Mostrar el ProgressBar
            _error.value = null     // Limpiar errores anteriores

            // Llama a la función del repositorio para obtener los datos.
            // Esta línea ahora funciona porque 'repository' es una instancia válida.
            val result = repository.fetchScoreboardFromUrl(url)

            // Gestiona el resultado de la llamada.
            result.onSuccess { standingsList ->
                // Si la llamada fue exitosa, actualiza el LiveData con la lista de equipos.
                _standings.value = standingsList
            }.onFailure { exception ->
                // Si falló, actualiza el LiveData de error con un mensaje.
                _error.value = "Error al cargar la clasificación: ${exception.message}"
            }

            _isLoading.value = false // Ocultar el ProgressBar
        }
    }
}
