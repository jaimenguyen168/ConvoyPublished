package edu.temple.convoy.main_convoy.audio

import java.io.File

interface AudioPlayer {
    fun playAudio(inputFile: File)
    fun stopAudio()
}