package edu.temple.convoy.main_convoy.audio

import android.net.Uri
import edu.temple.convoy.main_convoy.audio.component.AudioMessage

interface AudioPlayer {
    fun playAudio(audioMessage: AudioMessage)
    fun stopAudio()
}