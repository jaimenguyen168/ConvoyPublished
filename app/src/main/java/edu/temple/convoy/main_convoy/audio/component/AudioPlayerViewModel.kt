package edu.temple.convoy.main_convoy.audio.component

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory

data class AudioMessage(val id: Long = 0L, val username: String, val fileUri: Uri, val hasBeenPlayed: Boolean = false)
class AudioPlayerViewModel: ViewModel() {

    private val _audioMessages = MutableStateFlow<List<AudioMessage>>(emptyList())
    val audioMessages: StateFlow<List<AudioMessage>> = _audioMessages

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _selectedAudioMessage = MutableStateFlow<AudioMessage?>(null)
    val selectedAudioMessage: StateFlow<AudioMessage?> = _selectedAudioMessage

    fun selectAudioMessage(audioMessage: AudioMessage) {
        _selectedAudioMessage.value = audioMessage
    }

    fun releaseSelectedAudioMessage() {
        _selectedAudioMessage.value = null
    }

    fun clearAudioMessages() {
        _audioMessages.value = emptyList()
    }

    fun updateAudioMessagePlayedStatus(messageId: Long) {
        val updatedMessages = _audioMessages.value.map { message ->
            if (message.id == messageId) {
                message.copy(hasBeenPlayed = true)
            } else {
                message
            }
        }
        _audioMessages.value = updatedMessages
    }

    fun nowPlaying() {
        _isPlaying.value = true
        Log.d("AudioMessage in ViewModel", "Audio message in VM is playing. Value ${_isPlaying.value}")
    }

    fun nowStop() {
        _isPlaying.value = false
        Log.d("AudioMessage in ViewModel", "Audio message in VM has stopped. Value ${_isPlaying.value}")
    }

    private var messageIdCounter = 0L

    private fun generateMessageId(): Long = ++messageIdCounter

    suspend fun downloadAndAddMessage(context: Context, username: String, messageUrl: String) {
        viewModelScope.launch {
            val fileUri = downloadAudio(context, messageUrl)
            val audioMessage = fileUri?.let { AudioMessage(generateMessageId(), username, it) }
            audioMessage?.let { audioMessage ->
                _audioMessages.value += audioMessage
            }
        }
    }

    private suspend fun downloadAudio(context: Context, url: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // If the response code is OK, proceed with downloading
                    val audioFile = File(context.filesDir, "audio_file.mp3")
                    val inputStream = BufferedInputStream(connection.inputStream)
                    val outputStream = FileOutputStream(audioFile)
                    inputStream.copyTo(outputStream)
                    return@withContext Uri.fromFile(audioFile)
                } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                    // If the response code is 301, follow the redirect
                    val redirectUrl = connection.getHeaderField("Location")
                    Log.d("Redirect URL", redirectUrl)
                    return@withContext downloadAudio(
                        context,
                        redirectUrl
                    ) // Recursively call downloadAudio with the new URL
                } else {
                    Log.e("Download Error", "Server returned HTTP response code: $responseCode")
                }
            } catch (e: IOException) {
                Log.e("Download Error", "Error downloading audio file: ${e.message}")
            }
            return@withContext null
        }
    }
}