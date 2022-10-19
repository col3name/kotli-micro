package com.col3name.order.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(val id: Long?, val productId: Long)