package com.example.client

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Runner(
    val productClient: ProductClient,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        runBlocking {
            val product = Product(name = "GiroAccount", color = "green")
            println("Saving $product")
            val savedProduct = productClient.save(product)
            println("Saved $savedProduct")
            println("Find by id: ${productClient.findById(savedProduct?.id ?: 0)}")
//            println(productClient.findById(100))
            println("List all: ${productClient.findAll().toList()}")
        }
    }
}
