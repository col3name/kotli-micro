package com.col3name.customer.service

import com.col3name.customer.model.Customer
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class CustomerService(val db: JdbcTemplate) {
    fun findCustomers(): List<Customer> = db.query("select * from customers") { response, _ ->
        Customer(response.getLong("id"), response.getString("name"))
    }

    fun save(product: Customer) {
        db.update(
            "insert into customers (name) values (?);",
            product.name
        )
    }
//
//    fun findProductById(id: String): List<Product> {
//        val user: Product = db.query("SELECT * FROM products WHERE ID = :id")
//            .bind("id", 1)
//            .fetchOne(Product::class.java)
//    }
//    { response, _ ->
//        Product(response.getLong("id"), response.getString("name"))
//    }
}