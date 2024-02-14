package edu.temple.convoy.login_flow.data

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("session_key") val sessionKey: String?
)

data class ConvoyApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("convoy_id") val convoyId: String?,
    @SerializedName("firstname") val firstname: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("message") val message: String?
)