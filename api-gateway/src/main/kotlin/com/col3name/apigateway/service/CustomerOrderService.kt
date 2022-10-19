package com.col3name.apigateway.service

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.model.Order
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.util.*

@Service
class CustomerOrderService {
    fun fetchDataAsync(clientId: Long): CustomerOrder {
        val deferredCustomer = getCustomerDataAdapterAsync(clientId)
        val clientOrdersResponse = getClientOrdersAsync(clientId)

        var current = LocalDateTime.now().toString()

        println("done $current")
        val customer: Optional<CustomerDTO> = deferredCustomer
        val orders: List<Order> = clientOrdersResponse
        current = LocalDateTime.now().toString()
        println("wait $current")
        var customerName = ""
        if (customer.isPresent) {
            customerName = customer.get().name
        }
        return CustomerOrder(customerName, orders)
    }

    private fun getClientOrdersAsync(clientId: Long): List<Order> {
        val orders = getClientOrdersAdapter(clientId)
        println("orders")
        println(orders)

        val orderList = mapAndFetchOrders(orders)
        println("order list")
        println(orderList)
        return orderList
    }

    private fun mapAndFetchOrders(orders: List<OrderDTO>): List<Order> {
        val orderList: MutableList<Order> = ArrayList()
        orders.forEach { orderDTO ->
            val product = getProductDataAdapter(orderDTO.productId)
            val order = if (product.isEmpty) {
                Order(orderDTO.id, 0, "")
            } else {
                val item = product.get()
                Order(orderDTO.id, item.id, item.name)
            }
            orderList.add(order)
        }
        return orderList
    }

    private fun getClientOrdersAdapter(clientId: Long): List<OrderDTO> {
        val url = "http://localhost:8082/api/v1/orders?client_id=$clientId"
        val text = makeGetRequest(url)
        if (text.isEmpty()) {
            return listOf()
        }
        return Json.decodeFromString(text)
    }

    private fun getCustomerDataAdapterAsync(clientId: Long): Optional<CustomerDTO> {
        val url = "http://localhost:8081/api/v1/customers/$clientId"
        val text = makeGetRequest(url)
        if (text.isEmpty()) {
            return Optional.empty()
        }
        return Optional.of(Json.decodeFromString(text))
    }

    private fun getProductDataAdapter(productId: Long): Optional<ProductDTO> {
        val url = "http://localhost:8080/api/v1/products/$productId"
        val text = makeGetRequest(url)
        if (text.isEmpty()) {
            return Optional.empty()
        }
        return Optional.of(Json.decodeFromString(text))
    }

    private fun makeGetRequest(url: String): String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()

        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            println(url)
            if (response.statusCode() >= 400) {
                return ""
            }
            return response.body()
        } catch (e: Exception) {
            //TODO add slf4j
            println(e)
            return ""
        }
    }
}

@Serializable
data class CustomerDTO(val id: Long, val name: String)

@Serializable
data class ProductDTO(val id: Long, val name: String)

@Serializable
data class OrderDTO(val id: Long, val productId: Long)
