package com.col3name.product.product.controller

import com.col3name.product.product.model.Product
import com.col3name.product.product.service.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/products")
class QueryController(
    val productService: ProductService,
    val kafkaService: KafkaService,
    val template: KafkaTemplate<String?, String?>
) {
    @GetMapping("")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "name", defaultValue = "User") name: String
    ): ResponseEntity<List<Product>> {
        val body = productService.findProducts()
        val status = HttpStatus.OK
        val url = "/api/v1/products?name=$name"
        kafkaService.send(url, headers, status, Response(body, status.value()))

        return ResponseEntity(body, status)
    }

    @GetMapping("/{id}")
    fun getById(
        @RequestHeader headers: HttpHeaders,
        @PathVariable id: Long
    ): ResponseEntity<Product> {
        val product = productService.findProductById(id)
        var status = HttpStatus.NOT_FOUND
        val url = "/api/v1/products/${id}"

        if (product.isEmpty) {
            kafkaService.send(url, headers, status, "")
            return ResponseEntity(status)
        }

        status = HttpStatus.OK
        val body = product.get()
        val data = Message(
            Request(url, headers.toString()),
            Response(body, status.value())
        )
        template.send("topic1", Json.encodeToString(data))

        return ResponseEntity(body, status)
    }

    @PostMapping("")
    fun store(@RequestBody product: Product) {
        productService.save(product)
    }
}