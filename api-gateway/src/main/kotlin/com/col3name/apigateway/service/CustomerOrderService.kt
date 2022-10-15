package com.col3name.apigateway.service

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.model.Order
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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

    private fun getCustomerDataAdapter(clientId: Long): CustomerDTO {
        //TODO refactor to adapter and make http request
        val url = "http://localhost:8081/api/v1/customers/$clientId"
        println(makeGetRequest(url))
        return CustomerDTO(clientId, "mikha")
    }

    private fun getProductDataAdapter(productId: Long): ProductDTO {
        //TODO refactor to adapter and make http request
        val url = "http://localhost:8080/api/v1/products/$productId"
        println(makeGetRequest(url))
        return ProductDTO(1, "macbook pro 14.2 16gb 512gb")
    }

    private fun getClientOrdersAdapter(clientId: Long): List<OrderDTO> {
        //TODO refactor to adapter and make http request
        val url = "http://localhost:8082/api/v1/orders?client_id=$clientId"
        println(makeGetRequest(url))
        return listOf(
            OrderDTO(1, 1),
            OrderDTO(2, 2)
        )
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

data class CustomerDTO(val id: Long, val name: String)
data class ProductDTO(val id: Long, val name: String)
data class OrderDTO(val id: Long, val productId: Long)
