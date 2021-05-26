package com.juanmacapuano.clonetwitter.service.repository

import com.juanmacapuano.clonetwitter.service.api.Resource
import com.juanmacapuano.clonetwitter.service.api.apiSwagger
import com.juanmacapuano.clonetwitter.service.data.RequestLogin
import com.juanmacapuano.clonetwitter.service.data.ResponseLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class Repository() {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T>{
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }

    suspend fun loginUser(requestLogin: RequestLogin) = safeApiCall { apiSwagger.doLogin(requestLogin) }

}