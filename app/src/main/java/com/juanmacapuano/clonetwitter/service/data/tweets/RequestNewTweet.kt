package com.juanmacapuano.clonetwitter.service.data.tweets

import com.google.gson.annotations.SerializedName

data class RequestNewTweet(
    @SerializedName("mensaje")
    val mensaje: String
)
