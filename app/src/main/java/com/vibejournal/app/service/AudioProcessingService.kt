package com.vibejournal.app.service

import com.vibejournal.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class AudioProcessingService {
    
    private val client = OkHttpClient()
    private val openAiApiKey = BuildConfig.OPENAI_API_KEY
    
    suspend fun transcribeAudio(audioFile: File): String = withContext(Dispatchers.IO) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                audioFile.name,
                audioFile.asRequestBody("audio/3gp".toMediaType())
            )
            .addFormDataPart("model", "whisper-1")
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/audio/transcriptions")
            .addHeader("Authorization", "Bearer $openAiApiKey")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Transcription failed: ${response.code}")
            
            val responseBody = response.body?.string() ?: throw IOException("Empty response")
            val jsonResponse = JSONObject(responseBody)
            jsonResponse.getString("text")
        }
    }

    suspend fun generateJournalEntry(transcription: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            Please take the following transcribed thoughts and turn them into a coherent journal entry. 
            Make it flow naturally while preserving the original meaning and emotion. 
            Format it as a personal journal entry with proper paragraphs:
            
            "$transcription"
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            put("model", "gpt-4")
            put("messages", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", "You are a helpful assistant that transforms spoken thoughts into well-written journal entries.")
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("max_tokens", 1000)
            put("temperature", 0.7)
        }

        val requestBody = RequestBody.create(
            "application/json".toMediaType(),
            jsonBody.toString()
        )

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $openAiApiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Journal generation failed: ${response.code}")
            
            val responseBody = response.body?.string() ?: throw IOException("Empty response")
            val jsonResponse = JSONObject(responseBody)
            val choices = jsonResponse.getJSONArray("choices")
            val firstChoice = choices.getJSONObject(0)
            val message = firstChoice.getJSONObject("message")
            message.getString("content").trim()
        }
    }
}
