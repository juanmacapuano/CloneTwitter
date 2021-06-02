package com.juanmacapuano.clonetwitter.service.data.tweets

import com.google.gson.annotations.SerializedName

data class Like(
    @SerializedName("likes")
    val likes: List<User>
)
