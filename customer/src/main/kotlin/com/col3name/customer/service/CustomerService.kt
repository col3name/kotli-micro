package com.col3name.customer.service

import com.col3name.customer.model.Customer
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

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

    fun findCustomer(customerId: Long): Optional<Customer> {
        val customers = db.query("select * from customers where id = $customerId") { response, _ ->
            Customer(response.getLong("id"), response.getString("name"))
        }
        println(customers)
        if (customers.isEmpty()) {
            return Optional.empty()
        }
        return Optional.of(customers.first())
    }

}