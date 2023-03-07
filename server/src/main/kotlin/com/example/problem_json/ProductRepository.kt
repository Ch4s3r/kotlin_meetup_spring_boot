package com.example.problem_json

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CoroutineCrudRepository<Product, Long> {

    // Some examples not used in this presentation
    suspend fun findProductById(id: Long): Product

    fun findByName(name: String): Flow<Product>

    suspend fun findAllByName(name: String): List<Product>
}
