package com.col3name.order.controller

import com.col3name.order.model.Order
import com.col3name.order.service.Message
import com.col3name.order.service.OrderService
import com.col3name.order.service.Request
import com.col3name.order.service.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/orders")
class QueryController(
    val orderService: OrderService,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val kafkaTopic = "topic1"

    @GetMapping("")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "client_id", defaultValue = "0") clientId: Long
    ): ResponseEntity<List<Order>> {
        val url = "/api/v1/orders?client_id=$clientId"
        val orders = orderService.findClientOrders(clientId)

        val status = HttpStatus.OK
        val data = Message(
            Request(url, headers.toString()),
            Response(orders, status.value())
        )
        kafkaTemplate.send(kafkaTopic, Json.encodeToString(data))
        return ResponseEntity(orders, status)
    }

    @PostMapping("")
    fun store(@RequestBody order: Order) {
        orderService.save(order)
    }
}