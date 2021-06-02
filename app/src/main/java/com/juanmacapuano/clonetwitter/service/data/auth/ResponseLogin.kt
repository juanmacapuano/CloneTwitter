package com.juanmacapuano.clonetwitter.service.data.auth

data class ResponseLogin(val token: String,
                         val username: String,
                         val email: String,
                         val photoUrl: String,
                         val created: String,
                         val active: Boolean)
