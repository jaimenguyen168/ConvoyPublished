package edu.temple.convoy.main_convoy.recorder

import java.io.File

interface AudioRecorder {
    fun startRecording(outputFile: File)
    fun stopRecording()
}