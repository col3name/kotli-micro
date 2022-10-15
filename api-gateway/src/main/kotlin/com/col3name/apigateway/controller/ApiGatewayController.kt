package com.col3name.apigateway.controller

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.service.CustomerOrderService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping("/api/v1")
class ApiGatewayController(val customerOrderService: CustomerOrderService) {
    @GetMapping("/orders")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "client_id", defaultValue = "1") clientId: Long
    ): ResponseEntity<CustomerOrder> {
        return try {
            ResponseEntity(customerOrderService.get(clientId), HttpStatus.OK)
        } catch (e: Exception) {
            println(e)
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}