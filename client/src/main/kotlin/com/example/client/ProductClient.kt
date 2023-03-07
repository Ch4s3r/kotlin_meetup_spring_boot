package com.example.client

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange("/product")
interface ProductClient {
    @GetExchange("/{id}")
    suspend fun findById(
        @PathVariable id: Long,
    ): Product?

    @PostExchange
    suspend fun save(
        @RequestBody product: Product,
    ): Product?

    @GetExchange
    fun findAll(): Flow<Product>
}
