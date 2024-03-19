package edu.temple.convoy.main_convoy.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import edu.temple.convoy.main_convoy.audio.component.AudioMessage
import edu.temple.convoy.main_convoy.audio.component.AudioPlayerViewModel
import java.io.File

class ConvoyAudioPlayer(
    private val context: Context,
    private val viewModel: AudioPlayerViewModel
): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playAudio(audioMessage: AudioMessage) {
        player?.release() // Release previous player instance if exists
        viewModel.nowPlaying()

        player = MediaPlayer().apply {
            setDataSource(context, audioMessage.fileUri)
            setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }
            setOnCompletionListener {
                viewModel.updateAudioMessagePlayedStatus(audioMessage.id)
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
        viewModel.nowStop()
    }
}