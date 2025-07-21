package com.vibejournal.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.vibejournal.app.model.GoogleDoc
import com.vibejournal.app.service.AudioProcessingService
import com.vibejournal.app.service.GoogleDocsService
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val audioProcessingService = AudioProcessingService()
    private val googleDocsService = GoogleDocsService()

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean> = _isSignedIn

    private val _selectedJournal = MutableLiveData<GoogleDoc?>()
    val selectedJournal: LiveData<GoogleDoc?> = _selectedJournal

    private val _isProcessing = MutableLiveData<Boolean>()
    val isProcessing: LiveData<Boolean> = _isProcessing

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    init {
        checkSignInState()
    }

    private fun checkSignInState() {
        val account = GoogleSignIn.getLastSignedInAccount(getApplication())
        _isSignedIn.value = account != null
        if (account != null) {
            viewModelScope.launch {
                googleDocsService.setAccount(account)
            }
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult()
            _isSignedIn.value = true
            viewModelScope.launch {
                googleDocsService.setAccount(account)
            }
            _status.value = "Signed in successfully"
        } catch (e: Exception) {
            _isSignedIn.value = false
            _status.value = "Sign in failed: ${e.message}"
        }
    }

    fun setSelectedJournal(journal: GoogleDoc) {
        _selectedJournal.value = journal
    }

    fun processAudioFile(audioFile: File) {
        _isProcessing.value = true
        _status.value = "Transcribing audio..."

        viewModelScope.launch {
            try {
                // Step 1: Transcribe audio
                val transcription = audioProcessingService.transcribeAudio(audioFile)
                _status.value = "Generating journal entry..."

                // Step 2: Generate journal entry
                val journalEntry = audioProcessingService.generateJournalEntry(transcription)
                _status.value = "Saving to journal..."

                // Step 3: Save to Google Docs
                selectedJournal.value?.let { journal ->
                    googleDocsService.appendToDocument(journal.id, journalEntry)
                    _status.value = "Saved successfully!"
                } ?: run {
                    _status.value = "No journal selected"
                }

                // Clean up audio file
                audioFile.delete()

            } catch (e: Exception) {
                _status.value = "Error: ${e.message}"
            } finally {
                _isProcessing.value = false
            }
        }
    }
}
