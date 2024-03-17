package edu.temple.convoy.main_convoy.audio

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ConvoyAudioRecorder(
    private val context: Context
): AudioRecorder {

    private var recorder: MediaRecorder? = null
    private fun createRecorder(): MediaRecorder = MediaRecorder(context)

    override fun startRecording(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            try {
                prepare()
                start()
                recorder = this
            } catch (e: IOException) {
                Log.e("TAG", "prepare() failed")
            }
        }
    }

    override fun stopRecording() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }

}