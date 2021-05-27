package com.juanmacapuano.clonetwitter.service.data

data class ResponseSignup(
    val username: String,
    val email: String,
    val role: String,
    val photoUrl: String,
    val create: String,
    val active: String
)
