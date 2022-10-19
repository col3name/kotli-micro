package com.col3name.apigateway.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(val id: Long?, var productId: Long, var productTitle: String)

@Serializable
data class CustomerOrder(
    val customerName: String,
    val orders: List<Order>
)
