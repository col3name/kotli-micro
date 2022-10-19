package com.col3name.product.product.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(val id: Long?, val name: String)