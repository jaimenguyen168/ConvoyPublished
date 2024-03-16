package edu.temple.convoy.main_convoy.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class ConvoyAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null
    override fun playAudio(inputFile: File) {
        MediaPlayer.create(context, inputFile.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stopAudio() {
        player?.stop()
        player?.release()
        player = null
    }
}