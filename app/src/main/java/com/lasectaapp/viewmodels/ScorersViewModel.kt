package com.lasectaapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasectaapp.data.ScorersRepository
import com.lasectaapp.model.ScorersModels
import kotlinx.coroutines.launch

class ScorersViewModel : ViewModel() {

    private val repository = ScorersRepository()

    // LiveData para la lista de goleadores
    private val _scorers = MutableLiveData<List<ScorersModels>>()
    val scorers: LiveData<List<ScorersModels>> = _scorers

    // LiveData para controlar la visibilidad del ProgressBar
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para mostrar mensajes de error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Función que el FragmentGoleadores llamará para iniciar la carga de datos.
     */
    fun loadScorers(url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.fetchScorersFromUrl(url)

            result.onSuccess { scorersList ->
                // Éxito: actualiza el LiveData con la lista de goleadores
                _scorers.value = scorersList
            }.onFailure { exception ->
                // Fallo: actualiza el LiveData de error
                _error.value = "Error al cargar los goleadores: ${exception.message}"
            }

            _isLoading.value = false
        }
    }
}
