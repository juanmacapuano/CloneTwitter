package com.juanmacapuano.clonetwitter.service.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RemoteDataSource @Inject constructor(){

    companion object {
        const val BASE_URL = "https://minitwitter.com:3001/apiv1/"
    }

    fun <Api> buildAPI(
        api: Class<Api>,
        authToken: String? = null
    ) : Api
    {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val chain = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Authorization", "Bearer $authToken")
                }.build())
            }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .client(chain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}