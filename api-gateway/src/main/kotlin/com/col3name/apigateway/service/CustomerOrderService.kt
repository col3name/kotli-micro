package com.col3name.apigateway.service

import com.col3name.apigateway.model.CustomerOrder
import com.col3name.apigateway.model.Order
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import java.lang.Exception
import java.util.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class CustomerOrderService {
    private val logger: Logger = LoggerFactory.getLogger(CustomerOrderService::class.java)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 10)
            modifyRequest { request ->
                request.headers.append("x-retry-count", retryCount.toString())
            }
            delayMillis { retry ->
                retry * 3000L
            }
        }
    }

    fun fetchDataAsync(clientId: Long): CustomerOrder {
        val customerOrder: CustomerOrder
        runBlocking {
            val deferredCustomer = getCustomerDataAdapter(clientId)
            val customer: Optional<CustomerDTO> = deferredCustomer
            val orders = getClientOrdersAsync(clientId)
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

    private suspend fun getClientOrdersAdapter(clientId: Long): List<OrderDTO> {
        val url = "http://localhost:8082/api/v1/orders?client_id=$clientId"
        return try {
            client.get(url).body()
        } catch (e: Exception) {
            logger.error(e.message)
            return listOf()
        }
    }

    private suspend fun getCustomerDataAdapter(clientId: Long): Optional<CustomerDTO> {
        val url = "http://localhost:8081/api/v1/customers/$clientId"
        return try {
            val response: CustomerDTO = client.get(url).body()
            Optional.of(response)
        } catch (e: Exception) {
            logger.error(e.message)
            Optional.empty()
        }
    }

    private suspend fun getProductDataAdapter(productId: Long): Optional<ProductDTO> {
        val url = "http://localhost:8080/api/v1/products/$productId"
        return try {
            val response: ProductDTO = client.get(url).body()
            Optional.of(response)
        } catch (e: Exception) {
            logger.error(e.message)
            Optional.empty()
        }
    }
}

@Serializable
data class CustomerDTO(val id: Long, val name: String)

@Serializable
data class ProductDTO(val id: Long, val name: String)

@Serializable
data class OrderDTO(val id: Long, val productId: Long)
