package com.juanmacapuano.clonetwitter.service.data.auth

data class ResponseSignup(
    val token: String,
    val username: String,
    val email: String,
    val role: String,
    val photoUrl: String,
    val created: String,
    val active: String
)
