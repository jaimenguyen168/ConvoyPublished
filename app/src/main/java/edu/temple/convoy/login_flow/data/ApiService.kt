package edu.temple.convoy.login_flow.data

import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ApiService {
    @FormUrlEncoded
    @POST("account.php")
    suspend fun registerUser(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("password") password: String
    ): ApiResponse

    @FormUrlEncoded
    @POST("account.php")
    suspend fun loginUser(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse

    @FormUrlEncoded
    @POST("account.php")
    suspend fun logoutUser(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String
    ): ApiResponse

    @FormUrlEncoded
    @POST("convoy.php")
    suspend fun createConvoy(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String
    ): ConvoyApiResponse

    @FormUrlEncoded
    @POST("convoy.php")
    suspend fun endConvoy(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyId: String
    ): ConvoyApiResponse

    @FormUrlEncoded
    @POST("convoy.php")
    suspend fun joinConvoy(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyId: String
    ): ConvoyApiResponse

    @FormUrlEncoded
    @POST("convoy.php")
    suspend fun leaveConvoy(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyId: String
    ): ConvoyApiResponse

    @FormUrlEncoded
    @POST("convoy.php")
    suspend fun updateMyLocation(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): ConvoyApiResponse

    @FormUrlEncoded
    @POST("account.php")
    suspend fun updateFcmToken(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("fcm_token") fcmToken: String
    ): ConvoyApiResponse

    @Multipart
    @POST("convoy.php")
    suspend fun sendAudioMessage(
        @Part("action") action: String,
        @Part("username") username: String,
        @Part("session_key") sessionKey: String,
        @Part("convoy_id") convoyId: String,
        @Part messageFile: MultipartBody.Part
    ): ConvoyApiResponse
}