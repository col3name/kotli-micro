package com.col3name.apigateway.service

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.model.Order
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

@Service
class CustomerOrderService {
    fun fetchDataAsync(clientId: Long): CustomerOrder {
        val deferredCustomer = getCustomerDataAdapter(clientId)
        val customer: Optional<CustomerDTO> = deferredCustomer
        var orders: List<Order>
        val customerOrder: CustomerOrder
        runBlocking {
            orders = getClientOrdersAsync(clientId)
            var customerName = ""
            if (customer.isPresent) {
                customerName = customer.get().name
            }

            customerOrder = CustomerOrder(customerName, orders)
        }
        Thread.sleep(500)
        return customerOrder
    }

    private suspend fun getClientOrdersAsync(clientId: Long): List<Order> {
        val orders = getClientOrdersAdapter(clientId)
        val channel = Channel<Order>(10)

        mapAndFetchOrdersAsync(orders, channel)
        var allOrderList = emptyList<Order>()
        repeat(orders.size) {
            allOrderList = (allOrderList + channel.receive())
        }

        return allOrderList
    }

    private suspend fun mapAndFetchOrdersAsync(orders: List<OrderDTO>, channel: Channel<Order>) = coroutineScope {
        //TODO call api with retrofit
        orders.map { orderDTO ->
            launch {
                val product = getProductDataAdapter(orderDTO.productId)
                val order = if (product.isEmpty) {
                    Order(orderDTO.id, 0, "")
                } else {
                    val item = product.get()
                    Order(orderDTO.id, item.id, item.name)
                }
                channel.send(order)
            }
        }
    }

    private fun getClientOrdersAdapter(clientId: Long): List<OrderDTO> {
        val url = "http://localhost:8082/api/v1/orders?client_id=$clientId"
        val text = makeGetRequest(url)
        if (text.isEmpty()) {
            return listOf()
        }
        return Json.decodeFromString(text)
    }

    private fun getCustomerDataAdapter(clientId: Long): Optional<CustomerDTO> {
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
