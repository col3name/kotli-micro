package com.col3name.apigateway.controller

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.service.CustomerOrderService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ApiGatewayController(val customerOrderService: CustomerOrderService) {
    @GetMapping("/orders")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "client_id", defaultValue = "1") clientId: Long
    ): CustomerOrder {
        return customerOrderService.get(clientId)
    }
}