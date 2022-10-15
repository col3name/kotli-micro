package com.col3name.order.controller

import com.col3name.order.model.Order
import com.col3name.order.service.OrderService
import org.springframework.http.HttpHeaders
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
    ): List<Order> {
        return orderService.findClientOrders(clientId)
    }

    @PostMapping("")
    fun store(@RequestBody order: Order) {
        orderService.save(order)
    }
}