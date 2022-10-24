package com.col3name.apigateway.controller

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.service.CustomerOrderService
import com.col3name.apigateway.service.Message
import com.col3name.apigateway.service.Request
import com.col3name.apigateway.service.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ApiGatewayController(
    val customerOrderService: CustomerOrderService,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val logger: Logger = LoggerFactory.getLogger(CustomerOrderService::class.java)
    private val kafkaTopic = "topic1"

    @GetMapping("/orders")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "client_id", defaultValue = "-1") clientId: Long
    ): ResponseEntity<CustomerOrder> {
        var status = HttpStatus.OK
        val request = Request(getUrl(clientId), headers.toString())
        return try {
            val customerOrder = customerOrderService.fetchDataAsync(clientId)
            val data = Message(request, Response(customerOrder, status.value()))
            kafkaTemplate.send(kafkaTopic, Json.encodeToString(data))

            ResponseEntity(customerOrder, status)
        } catch (e: Exception) {
            logger.error(e.toString())
            status = HttpStatus.NOT_FOUND
            val data = Message(request, Response("", status.value()))
            kafkaTemplate.send(kafkaTopic, Json.encodeToString(data))

            ResponseEntity(status)
        }
    }

    private fun getUrl(clientId: Long): String {
        return "/api/v1/orders?client_id=$clientId"
    }
}