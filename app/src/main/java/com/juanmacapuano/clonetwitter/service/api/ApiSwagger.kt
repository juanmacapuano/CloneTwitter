package com.juanmacapuano.clonetwitter.service.api

import com.juanmacapuano.clonetwitter.service.data.auth.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.auth.RequestSignup
import com.juanmacapuano.clonetwitter.service.data.auth.ResponseLogin
import com.juanmacapuano.clonetwitter.service.data.auth.ResponseSignup
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiSwagger {

    @POST(value = "auth/login")
    suspend fun doLogin(@Body requestLogin: RequestLogin): ResponseLogin

    @POST(value = "auth/signup")
    suspend fun doSignUp(@Body requestSignUp: RequestSignup): ResponseSignup

    @GET(value = "tweets/all")
    suspend fun getAllTweets() : List<Tweet>

}
