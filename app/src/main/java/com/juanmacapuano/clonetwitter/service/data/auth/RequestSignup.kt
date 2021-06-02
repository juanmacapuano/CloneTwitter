package com.juanmacapuano.clonetwitter.service.data.auth

data class RequestSignup(val username: String,
                         val email: String,
                         val password: String,
                         val code: String?
                        )
