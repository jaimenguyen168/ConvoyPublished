package edu.temple.convoy.main_convoy.fcm

import kotlinx.serialization.Serializable
@Serializable
data class ConvoyParticipant(
    val username: String,
    val firstname: String,
    val lastname: String,
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class MessageReceived(
    val action: String,
    val data: List<ConvoyParticipant>? = null,
    val convoy_id: String? = null
)
