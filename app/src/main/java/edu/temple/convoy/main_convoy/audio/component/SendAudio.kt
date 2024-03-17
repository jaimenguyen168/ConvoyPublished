package edu.temple.convoy.main_convoy.audio.component

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


object SendAudio {
    suspend fun sendVoiceMessage(
        username: String,
        sessionKey: String,
        convoyId: String,
        audioFile: File,
        onResponse: (success: Boolean, errorMessage: String?, convoyId: String?) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val url = URL("https://kamorris.com/lab/convoy/convoy.php")
            val connection = url.openConnection() as HttpURLConnection

            connection.doOutput = true
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "multipart/form-data")

            val boundary = "*****"
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")

            val outputStream = DataOutputStream(connection.outputStream)

            try {
                outputStream.writeBytes("--$boundary\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"action\"\r\n\r\n")
                outputStream.writeBytes("MESSAGE\r\n")
                outputStream.writeBytes("--$boundary\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"username\"\r\n\r\n")
                outputStream.writeBytes("$username\r\n")
                outputStream.writeBytes("--$boundary\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"session_key\"\r\n\r\n")
                outputStream.writeBytes("$sessionKey\r\n")
                outputStream.writeBytes("--$boundary\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"convoy_id\"\r\n\r\n")
                outputStream.writeBytes("$convoyId\r\n")
                outputStream.writeBytes("--$boundary\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"message_file\";filename=\"${audioFile.name}\"\r\n")
                outputStream.writeBytes("\r\n")

                // Write the file
                val fileInputStream = FileInputStream(audioFile)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                fileInputStream.close()

                outputStream.writeBytes("\r\n")
                outputStream.writeBytes("--$boundary--\r\n")
                outputStream.flush()

                // Get response
                val responseCode = connection.responseCode
                val responseMessage = connection.responseMessage
                val inputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                // Parse response
                val jsonResponse = response.toString()
                if (jsonResponse.contains("SUCCESS")) {
                    onResponse(true, null, convoyId)
                } else {
                    onResponse(false, responseMessage, null)
                }
            } catch (e: Exception) {
                onResponse(false, e.message, null)
            } finally {
                outputStream.close()
                connection.disconnect()
            }
        }
    }
}