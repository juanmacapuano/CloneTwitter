package com.juanmacapuano.clonetwitter.service.api

import com.juanmacapuano.clonetwitter.service.data.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.RequestSignup
import com.juanmacapuano.clonetwitter.service.data.ResponseLogin
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiSwagger {

    companion object {
        const val BASE_URL = "https://minitwitter.com:3001/apiv1/"
        const val CODE = "UDEMYANDROID"
    }

    @POST(value = "auth/login")
    suspend fun doLogin(@Body requestLogin: RequestLogin): ResponseLogin

    @POST(value = "auth/signup")
    suspend fun doSignUp(@Body requestSignUp: RequestSignup): ResponseLogin

}

fun create(): ApiSwagger {
    val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    return Retrofit.Builder()
        .baseUrl(ApiSwagger.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiSwagger::class.java)

}

var apiSwagger : ApiSwagger = create()