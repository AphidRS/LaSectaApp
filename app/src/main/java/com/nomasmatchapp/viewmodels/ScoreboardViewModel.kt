package com.nomasmatchapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nomasmatchapp.data.ScoreboardRepository
import com.nomasmatchapp.model.ScoreboardModels
import kotlinx.coroutines.launch

class ScoreboardViewModel : ViewModel() {

    private val repository = ScoreboardRepository()
    private val _standings = MutableLiveData<List<ScoreboardModels>>()
    val standings: LiveData<List<ScoreboardModels>> = _standings
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadClassification(url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.fetchScoreboardFromUrl(url)

            result.onSuccess { standingsList ->
                _standings.value = standingsList
            }.onFailure { exception ->
                _error.value = "Error al cargar la clasificación: ${exception.message}"
            }

            _isLoading.value = false
        }
    }
}
