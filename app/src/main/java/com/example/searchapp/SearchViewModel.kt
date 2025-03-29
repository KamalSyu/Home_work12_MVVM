package com.example.searchapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _resultText = MutableLiveData<String>()
    val resultText: LiveData<String> get() = _resultText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun search(query: String) {
        _isLoading.value = true
        coroutineScope.launch {
            delay(2000) // Имитация задержки запроса
            _isLoading.value = false
            _resultText.value = "По запросу \"$query\" ничего не найдено"
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel() // Отмена корутин при уничтожении ViewModel
    }
}