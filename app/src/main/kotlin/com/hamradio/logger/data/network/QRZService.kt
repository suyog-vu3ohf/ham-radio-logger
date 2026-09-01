package com.hamradio.logger.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class QRZLoginResponse(
    @SerializedName("Session")
    val session: QRZSession?
)

data class QRZSession(
    @SerializedName("Key")
    val key: String,
    @SerializedName("Message")
    val message: String?
)

data class QRZInsertResponse(
    @SerializedName("Result")
    val result: String?
)

interface QRZService {

    @FormUrlEncoded
    @POST("xmlrpc.php")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<QRZLoginResponse>

    @FormUrlEncoded
    @POST("xmlrpc.php")
    suspend fun insertLogEntry(
        @Field("key") sessionKey: String,
        @Field("call") callSign: String,
        @Field("qso_date") date: String,
        @Field("time_on") timeOn: String,
        @Field("time_off") timeOff: String,
        @Field("freq") frequency: String,
        @Field("mode") mode: String,
        @Field("rst_sent") rstSent: String,
        @Field("notes") notes: String? = null
    ): Response<QRZInsertResponse>
}
