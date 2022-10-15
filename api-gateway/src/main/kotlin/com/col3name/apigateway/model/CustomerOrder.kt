package com.col3name.apigateway.model

data class Order(val id: Long?, var productId: Long, var productTitle: String)

data class CustomerOrder(
    val customerName: String,
    val orders: List<Order>
)
