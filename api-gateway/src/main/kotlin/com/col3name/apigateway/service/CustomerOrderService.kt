package com.col3name.apigateway.service

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.model.Order
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Service
class CustomerOrderService {
    fun get(clientId: Long): CustomerOrder {
        val orders: List<OrderDTO> = getClientOrdersAdapter(clientId)

        println("orders")
        println(orders)
        val orderList = mapAndFetchOrders(orders)
        println("order list")
        println(orderList)
        val customer = getCustomerDataAdapter(clientId)
        //TODO parallel fetch data
        return CustomerOrder(customer.name, orderList)
    }

    private fun mapAndFetchOrders(orders: List<OrderDTO>): List<Order> {
        val orderList: MutableList<Order> = ArrayList()
        orders.forEach { order ->
            val product = getProductDataAdapter(order.productId)
            orderList.add(Order(order.id, product.id, product.name))
        }
        return orderList
    }

    private fun getClientOrdersAdapter(clientId: Long): List<OrderDTO> {
        val url = "http://localhost:8082/api/v1/orders?client_id=$clientId"
        val text = makeGetRequest(url)
        return Json.decodeFromString(text)
    }

    private fun getCustomerDataAdapter(clientId: Long): CustomerDTO {
        val url = "http://localhost:8081/api/v1/customers/$clientId"
        val text = makeGetRequest(url)
        return Json.decodeFromString(text)
    }

    private fun getProductDataAdapter(productId: Long): ProductDTO {
        val url = "http://localhost:8080/api/v1/products/$productId"
        val text = makeGetRequest(url)
        return Json.decodeFromString(text)
    }

    private fun makeGetRequest(url :String): String {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        println(url)
        return response.body()
    }
}

@Serializable
data class CustomerDTO(val id: Long, val name: String)

@Serializable
data class ProductDTO(val id: Long, val name: String)

@Serializable
data class OrderDTO(val id: Long, val productId: Long)
