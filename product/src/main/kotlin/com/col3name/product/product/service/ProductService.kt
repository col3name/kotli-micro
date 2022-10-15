package com.col3name.product.product.service

import com.col3name.product.product.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ProductService(val db: JdbcTemplate) {
    fun findProducts(): List<Product> = db.query("select * from products") { response, _ ->
        Product(response.getLong("id"), response.getString("name"))
    }

    fun findProductById(id: Long): List<Product> = db.query("select * from products where id = $id") { response, _ ->
        Product(response.getLong("id"), response.getString("name"))
    }

    fun save(product: Product) {
        db.update(
            "insert into products (name) values (?);",
            product.name
        )
    }
}