package com.col3name.customer.model

import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: Long?, val name: String)