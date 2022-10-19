package com.col3name.customer.controller

import com.col3name.customer.model.Customer
import com.col3name.customer.service.CustomerService
import com.col3name.customer.service.Message
import com.col3name.customer.service.Request
import com.col3name.customer.service.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(
    val customerService: CustomerService,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val kafkaTopic = "topic1"

    @GetMapping("")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "name", defaultValue = "User") name: String
    ): ResponseEntity<List<Customer>> {
        val url = "/api/v1/customers?name=$name"
        val body = customerService.findCustomers()
        val status = HttpStatus.OK
        val data = Message(
            Request(url, headers.toString()),
            Response(body, status.value())
        )
        kafkaTemplate.send(kafkaTopic, Json.encodeToString(data))

        return ResponseEntity(body, status)
    }

    @GetMapping("/{id}")
    fun getById(
        @RequestHeader headers: HttpHeaders,
        @PathVariable id: Long
    ): ResponseEntity<Customer> {
        val url = "/api/v1/customers/$id"
        val request = Request(url, headers.toString())

        val customer = customerService.findCustomer(id)
        if (customer.isEmpty) {
            val status = HttpStatus.NOT_FOUND
            val data = Message(request, Response("", status.value()))
            kafkaTemplate.send(kafkaTopic, Json.encodeToString(data))

            return ResponseEntity(status)
        }

        val status = HttpStatus.OK
        val body = customer.get()
        val data = Message(request, Response(body, status.value()))
        kafkaTemplate.send(kafkaTopic, Json.encodeToString(data))

        return ResponseEntity(customer.get(), status)
    }

    @PostMapping("")
    fun store(@RequestBody customer: Customer) {
        customerService.save(customer)
    }

}