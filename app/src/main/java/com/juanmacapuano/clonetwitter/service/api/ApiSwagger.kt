package com.juanmacapuano.clonetwitter.service.api

import com.juanmacapuano.clonetwitter.service.data.auth.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.auth.RequestSignup
import com.juanmacapuano.clonetwitter.service.data.auth.ResponseLogin
import com.juanmacapuano.clonetwitter.service.data.auth.ResponseSignup
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiSwagger {

    @POST(value = "auth/login")
    suspend fun doLogin(@Body requestLogin: RequestLogin): ResponseLogin

    @POST(value = "auth/signup")
    suspend fun doSignUp(@Body requestSignUp: RequestSignup): ResponseSignup

    @GET(value = "tweets/all")
    suspend fun getAllTweets() : List<Tweet>

    @POST("tweets/like/{idTweet}")
    suspend fun setLike(@Path("idTweet") idTweet: Int) : Tweet

}
