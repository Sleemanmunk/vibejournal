package com.vibejournal.app.service

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.JsonObject
import com.vibejournal.app.model.GoogleDoc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class GoogleDocsService {
    
    private val client = OkHttpClient()
    private var accessToken: String? = null
    private var selectedJournal: GoogleDoc? = null

    suspend fun setAccount(account: GoogleSignInAccount) {
        // For now, we'll use a simple placeholder approach
        // In a real implementation, you'd exchange the account for an access token
        accessToken = "placeholder_token"
    }

    suspend fun listDocuments(): List<GoogleDoc> = withContext(Dispatchers.IO) {
        // Return some sample documents for now
        listOf(
            GoogleDoc(
                id = "sample_doc_1",
                title = "My Journal - ${SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())}",
                createdTime = Date().toString()
            ),
            GoogleDoc(
                id = "sample_doc_2", 
                title = "Daily Thoughts",
                createdTime = Date().toString()
            )
        )
    }

    suspend fun createDocument(title: String): GoogleDoc = withContext(Dispatchers.IO) {
        GoogleDoc(
            id = "new_doc_${System.currentTimeMillis()}",
            title = title,
            createdTime = Date().toString()
        )
    }

    suspend fun appendToDocument(documentId: String, content: String) = withContext(Dispatchers.IO) {
        // For now, just log the content that would be appended
        val timestamp = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.getDefault())
            .format(Date())
        
        val fullContent = "\n\n--- $timestamp ---\n$content\n"
        println("Would append to document $documentId: $fullContent")
    }

    fun setSelectedJournal(journal: GoogleDoc) {
        selectedJournal = journal
    }

    fun getSelectedJournal(): GoogleDoc? = selectedJournal
}
