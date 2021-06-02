package com.juanmacapuano.clonetwitter.service.api

import okhttp3.Response
import okhttp3.ResponseBody

sealed class Resource<out T> {

    data class Success<out T>(val value: T): Resource<T>()
    data class Failure(
        val inNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val message: String?
    ): Resource<Nothing>()
}