package com.col3name.product.product.service

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Serializable
data class Request(var url: String, var headers: String)

@Serializable
data class Response<T>(val response: T, val status: Int)

@Serializable
data class Message<T>(val request: Request, val response: Response<T>)

@Service
class KafkaService(
    val template: KafkaTemplate<String?, String?>
) {
    fun <T> send(url: String, headers: HttpHeaders, status: HttpStatus, body: T) {
        val data = Message<T>(
            Request(url, headers.toString()),
            Response<T>(body, status.value())
        )
        template.send("topic1", Json.encodeToString(data))
    }
}