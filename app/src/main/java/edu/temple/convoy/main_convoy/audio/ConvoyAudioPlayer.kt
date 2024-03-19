package edu.temple.convoy.main_convoy.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import edu.temple.convoy.main_convoy.audio.component.AudioPlayerViewModel
import java.io.File

class ConvoyAudioPlayer(
    private val context: Context,
    private val viewModel: AudioPlayerViewModel
): AudioPlayer {

    private var player: MediaPlayer? = null
    var isPlaying: Boolean = false
        private set

    var currentAudioUri: Uri? = null

    override fun playAudio(fileUri: Uri) {
        player?.release() // Release previous player instance if exists
        viewModel.nowPlaying()

        player = MediaPlayer().apply {
            setDataSource(context, fileUri)
            setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
                currentAudioUri = fileUri
                this@ConvoyAudioPlayer.isPlaying = true
            }
            setOnCompletionListener {
                viewModel.nowStop()
                stopAudio()
            }
            setOnErrorListener { _, _, _ ->
                false
            }
            prepareAsync()
        }
    }

    override fun stopAudio() {
        player?.stop()
        player?.release()
        player = null
        currentAudioUri = null
        isPlaying = false
    }
}