package com.col3name.order.controller

import com.col3name.order.model.Order
import com.col3name.order.service.OrderService
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
    @GetMapping("")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "client_id", defaultValue = "0") clientId: Long
    ): ResponseEntity<List<Order>> {
        val orders = orderService.findClientOrders(clientId)
        return ResponseEntity(orders, HttpStatus.OK)
    }

    @PostMapping("")
    fun store(@RequestBody order: Order) {
        orderService.save(order)
    }
}