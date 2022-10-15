package com.col3name.product.product.controller

import com.col3name.product.product.model.Product
import com.col3name.product.product.service.ProductService
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
    val kafkaTemplate: KafkaTemplate<String, String>
) {

//     val topicFindAllRequest = "product.request.findAll"
//     val topicFindAllResponse = "product.response.findAll"

    @GetMapping("")
    fun index(
        @RequestHeader headers: HttpHeaders,
        @RequestParam(name = "name", defaultValue = "User") name: String
    ): ResponseEntity<List<Product>> {
        // let's print all
//        headers.forEach {
//            println("${it.key}: ${it.value}")
//        }
//        kafkaTemplate.send("topic1", "test")
//        kafkaTemplate.send("product.request.findAll", "GET /person/name OK > $name")
//         kafkaTemplate.send("product.request.findAll", "GET /person/name BadRequest > $name")
        return ResponseEntity(productService.findProducts(), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Product> {
        val customer = productService.findProductById(id)
        if (customer.isEmpty) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(customer.get(), HttpStatus.OK)
    }

    @PostMapping("")
    fun store(@RequestBody product: Product) {
        productService.save(product)
    }
}