package com.example.problem_json

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/product")
class ProductController(
    val productService: ProductService,
) {

    @GetMapping("/{id}")
    suspend fun findById(
        @PathVariable id: Long,
    ) =
        productService.findById(id)
//            ?: throw ResponseStatusException(NOT_FOUND, "Product not found")

    @PostMapping
    suspend fun save(
        @Valid @RequestBody
        product: Product,
    ) =
        productService.save(product)

    @GetMapping
    fun findAll() = productService.findAll()
}
