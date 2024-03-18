package edu.temple.convoy.main_convoy.audio

import android.net.Uri

interface AudioPlayer {
    fun playAudio(fileUri: Uri)
    fun stopAudio()
}