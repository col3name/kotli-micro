package com.col3name.apigateway

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListener

@SpringBootApplication
class ApiGatewayApplication {
    @Bean
    fun topic() = NewTopic("topic1", 1, 1)

    @KafkaListener(id = "myId", topics = ["topic1"])
    fun listen(value: String?) {
        println(value)
    }
}

fun main(args: Array<String>) {
    runApplication<ApiGatewayApplication>(*args)
}
