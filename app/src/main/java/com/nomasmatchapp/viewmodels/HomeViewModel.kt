package com.nomasmatchapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomasmatchapp.data.RoundsRepository
import com.nomasmatchapp.model.Round
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = RoundsRepository()

    // LiveData privado para los datos. Solo el ViewModel puede cambiarlo.
    private val _rounds = MutableLiveData<List<Round>>()
    // LiveData público para que el Fragment lo observe. Es inmutable.
    val rounds: LiveData<List<Round>> = _rounds

    // LiveData para comunicar el estado de carga (ej: mostrar una ProgressBar)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para comunicar errores a la UI.
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Inicia el proceso de obtención de datos desde la URL proporcionada.
     */
    fun loadRounds(url: String) {
        // Usamos viewModelScope para lanzar una corrutina que se cancela
        // automáticamente si el ViewModel se destruye.
        viewModelScope.launch {
            _isLoading.value = true // Empieza la carga
            _error.value = null // Limpia errores previos

            val result = repository.fetchRoundsFromUrl(url)

            // Manejamos el resultado que nos devuelve el Repository
            result.onSuccess { roundList ->
                // Si todo fue bien, actualizamos el LiveData con la lista de jornadas
                _rounds.value = roundList
            }.onFailure { exception ->
                // Si hubo un error, lo comunicamos a la UI
                _error.value = "Error al obtener los datos: ${exception.message}"
            }
            _isLoading.value = false // Termina la carga
        }
    }
}
