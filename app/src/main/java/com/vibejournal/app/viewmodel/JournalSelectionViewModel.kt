package com.vibejournal.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibejournal.app.model.GoogleDoc
import com.vibejournal.app.service.GoogleDocsService
import kotlinx.coroutines.launch

class JournalSelectionViewModel : ViewModel() {

    private val googleDocsService = GoogleDocsService()

    private val _journals = MutableLiveData<List<GoogleDoc>>()
    val journals: LiveData<List<GoogleDoc>> = _journals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadJournals() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val docs = googleDocsService.listDocuments()
                _journals.value = docs
            } catch (e: Exception) {
                _error.value = "Failed to load journals: ${e.message}"
                _journals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createNewJournal(title: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val newDoc = googleDocsService.createDocument(title)
                val currentList = _journals.value?.toMutableList() ?: mutableListOf()
                currentList.add(0, newDoc)
                _journals.value = currentList
            } catch (e: Exception) {
                _error.value = "Failed to create journal: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectJournal(journal: GoogleDoc) {
        // This would typically save the selection to preferences
        // For now, we'll use a simple approach
        googleDocsService.setSelectedJournal(journal)
    }
}
