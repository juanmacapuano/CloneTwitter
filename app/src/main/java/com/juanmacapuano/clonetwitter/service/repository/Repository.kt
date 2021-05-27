package com.juanmacapuano.clonetwitter.service.repository

import com.juanmacapuano.clonetwitter.service.UserPreferences
import com.juanmacapuano.clonetwitter.service.api.ApiSwagger
import com.juanmacapuano.clonetwitter.service.data.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.RequestSignup

class Repository(
    private val api: ApiSwagger,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun loginUser(requestLogin: RequestLogin) = safeApiCall { api.doLogin(requestLogin) }

    suspend fun registerUser(requestSignup: RequestSignup) = safeApiCall { api.doSignUp(requestSignup)}

    suspend fun saveAuth(token: String) = preferences.saveAuthToken(token)

}