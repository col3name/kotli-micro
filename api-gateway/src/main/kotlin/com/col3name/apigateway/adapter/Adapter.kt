package com.col3name.apigateway

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object Adapter {
    private var httpClient: HttpClient? = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            modifyRequest { request ->
                request.headers.append("x-retry-count", retryCount.toString())
            }
            delayMillis { retry ->
                retry * 550L
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 250
        }
        install(Auth) {
            bearer {
                BearerTokens("1", "2")
            }
        }
    }

    fun getClient(
    ): HttpClient {
        return httpClient!!
    }
}

