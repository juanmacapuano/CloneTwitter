package com.juanmacapuano.clonetwitter.service.repository

import com.juanmacapuano.clonetwitter.service.UserPreferences
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.data.auth.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.auth.RequestSignup
import com.juanmacapuano.clonetwitter.service.data.tweets.RequestNewTweet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class Repository @Inject constructor(
    private val api: ApiSwagger,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun loginUser(requestLogin: RequestLogin) = safeApiCall { api.doLogin(requestLogin) }

    suspend fun registerUser(requestSignup: RequestSignup) = safeApiCall { api.doSignUp(requestSignup)}

    suspend fun saveAuth(token: String) = preferences.saveAuthToken(token)

    suspend fun getAllTweets() = safeApiCall { api.getAllTweets()}

    suspend fun createTweet(requestNewTweet: RequestNewTweet) = safeApiCall { api.createTweet(requestNewTweet) }

    suspend fun likeTweet(idTweet: Int) = safeApiCall { api.likeTweet(idTweet) }

}