package com.dicoding.picodiploma.storylensapp.data.api

import android.util.Log
import com.dicoding.picodiploma.storylensapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/" // URL API

        fun getApiService(token: String): ApiService {
            // Logging token untuk debugging
            Log.d("ApiConfig", "Token value: $token")

            // Buat interceptor untuk logging
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            }

            // Buat interceptor untuk menambahkan header Authorization
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }

            // Konfigurasi OkHttpClient
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            // Konfigurasi Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}