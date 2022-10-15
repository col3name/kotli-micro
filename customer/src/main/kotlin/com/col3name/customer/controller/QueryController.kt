package com.col3name.customer.controller

import com.col3name.customer.model.Customer
import com.col3name.customer.service.CustomerService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.util.*
import  org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api/v1/customers")
class QueryController(
    val customerService: CustomerService,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    @GetMapping("")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "name", defaultValue = "User") name: String
    ): ResponseEntity<List<Customer>> {
        return ResponseEntity(customerService.findCustomers(), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Customer> {
        val customer = customerService.findCustomer(id)
        if (customer.isEmpty) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(customer.get(), HttpStatus.OK)
    }

    @PostMapping("")
    fun store(@RequestBody customer: Customer) {
        customerService.save(customer)
    }
}