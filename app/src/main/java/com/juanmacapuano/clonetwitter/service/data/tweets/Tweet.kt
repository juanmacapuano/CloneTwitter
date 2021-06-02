package com.juanmacapuano.clonetwitter.service.data.tweets

import com.google.gson.annotations.SerializedName

data class Tweet(
    @SerializedName("id")
    val id: Int,
    @SerializedName("mensaje")
    val message: String,
    @SerializedName("likes")
    val likes: List<User>,
    @SerializedName("user")
    val user: User
)
