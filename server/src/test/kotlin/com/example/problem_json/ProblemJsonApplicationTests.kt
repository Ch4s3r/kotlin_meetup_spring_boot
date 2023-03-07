package com.example.problem_json

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class ProblemJsonApplicationTests(
    val productRepository: ProductRepository,
) {

    @Test
    fun `can write into database`(): Unit = runBlocking {
        val product = Product(name = "test", color = "testcolor")
        val expected = product.copy(id = 1)

        val actual = productRepository.save(product)

        actual shouldBe expected
    }
}
