package com.col3name.product.product.controller

import com.col3name.product.product.model.Product
import com.col3name.product.product.service.ProductService
import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.http.HttpHeaders
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.util.MultiValueMap
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
    ): List<Product> {
        // let's print all
//        headers.forEach {
//            println("${it.key}: ${it.value}")
//        }
//        kafkaTemplate.send("topic1", "test")
//        kafkaTemplate.send("product.request.findAll", "GET /person/name OK > $name")
//         kafkaTemplate.send("product.request.findAll", "GET /person/name BadRequest > $name")
        return productService.findProducts()
//        return listOf(
//            Product(1, "macbook air 16gb 512gb 2020"),
//            Product(2, "macbook pro 14.2 16gb 512gb 2021")
//        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Product =
        productService.findProducts().first { product: Product -> product.id == id }

    @PostMapping("")
    fun store(@RequestBody product: Product) {
        productService.save(product)
    }
}