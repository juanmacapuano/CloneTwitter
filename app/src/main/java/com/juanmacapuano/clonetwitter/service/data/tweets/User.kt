package com.juanmacapuano.clonetwitter.service.data.tweets

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val userName: String,
    @SerializedName("descripcion")
    val description: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("created")
    val created: String
)
