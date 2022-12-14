package com.col3name.order.service

import com.col3name.order.model.Order
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.Types

@Service
class OrderService(val db: JdbcTemplate) {
    fun findClientOrders(clientId: Long): List<Order> =
        db.query("select * from orders where client_id = $clientId") { response, _ ->
            Order(response.getLong("id"), response.getLong("product_id"))
        }

    fun save(order: Order) {
        db.update(
            "insert into orders (product_id) values (?);",
            order.productId
        )
    }
}