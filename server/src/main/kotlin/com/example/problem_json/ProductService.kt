package com.example.problem_json

import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository,
) {

    suspend fun findById(
        id: Long,
    ) =
        productRepository.findById(id)

    suspend fun save(
        product: Product,
    ) =
        productRepository.save(product)

    fun findAll() =
        productRepository.findAll()
}
