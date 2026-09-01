package com.hamradio.logger.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val QRZ_BASE_URL = "https://xmlrpc.qrz.com/"
    private const val BAND_DATA_BASE_URL = "https://www.hamqsl.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val qrzRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(QRZ_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val bandDataRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BAND_DATA_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val qrzService: QRZService by lazy {
        qrzRetrofit.create(QRZService::class.java)
    }

    val bandDataService: BandDataService by lazy {
        bandDataRetrofit.create(BandDataService::class.java)
    }
}
