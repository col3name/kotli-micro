package com.col3name.customer.service

import kotlinx.serialization.Serializable

@Serializable
data class Request(var url: String, var headers: String)

@Serializable
data class Response<T>(val response: T, val status: Int)

@Serializable
data class Message<T>(val request: Request, val response: Response<T>)
