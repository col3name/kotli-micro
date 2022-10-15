package com.col3name.customer.controller

import com.col3name.customer.model.Customer
import com.col3name.customer.service.CustomerService
import org.springframework.http.HttpHeaders
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.util.*

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
    ): List<Customer> {
        return customerService.findCustomers()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Customer {
        val customers = customerService.findCustomers()
        if (customers.isEmpty() ){
            return Customer(1, "not found")
        }
        return customers.first { customer: Customer -> customer.id == id }
    }

    @PostMapping("")
    fun store(@RequestBody customer: Customer) {
        customerService.save(customer)
    }
}