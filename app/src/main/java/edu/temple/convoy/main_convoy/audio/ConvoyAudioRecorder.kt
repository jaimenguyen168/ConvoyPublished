package edu.temple.convoy.main_convoy.audio

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream

class ConvoyAudioRecorder(
    private val  context: Context
): AudioRecorder {

    private var recorder: MediaRecorder? = null
    private fun createRecorder(): MediaRecorder = MediaRecorder(context)

    override fun startRecording(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
        }
    }

    override fun stopRecording() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }

}