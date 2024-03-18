package edu.temple.convoy.main_convoy.fcm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ConvoyParticipant(
    val username: String,
    val firstname: String,
    val lastname: String,
    val latitude: Double,
    val longitude: Double
)

// Define base sealed class for different types of messages
//@Serializable
//sealed class MessageReceived {
//    abstract val action: String
//}
//
//// Define data classes for different message types
//@Serializable
//@SerialName("UPDATE")
//data class UpdateMessage(
//    override val action: String,
//    val data: List<ConvoyParticipant>
//) : MessageReceived()
//
//@Serializable
//@SerialName("END")
//data class EndMessage(
//    override val action: String,
//    val convoy_id: String
//) : MessageReceived()
//
//@Serializable
//@SerialName("MESSAGE")
//data class AudioMessage(
//    override val action: String,
//    val username: String,
//    val message_url: String
//) : MessageReceived()

@Serializable
data class MessageReceived(
    val action: String,
    val data: List<ConvoyParticipant>? = null,
    val convoy_id: String? = null,
    val username: String? = null,
    val message_file: String? = null
)
