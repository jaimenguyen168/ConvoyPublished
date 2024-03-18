package edu.temple.convoy.main_convoy.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import java.io.File

class ConvoyAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null
    override fun playAudio(fileUri: Uri) {
        player?.release() // Release previous player instance if exists

        player = MediaPlayer().apply {
            setDataSource(context, fileUri)
            setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }
            setOnErrorListener { _, _, _ ->
                false
            }
            prepareAsync() // Prepare asynchronously to avoid blocking the UI thread
        }
//        MediaPlayer.create(context, inputFile.toUri()).apply {
//            player = this
//            start()
//        }
    }

    override fun stopAudio() {
        player?.stop()
        player?.release()
        player = null
    }
}