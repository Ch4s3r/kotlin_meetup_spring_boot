package com.example.problem_json

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id

data class Product(
    @Id
    val id: Long = 0,
    @Size(min = 5)
    val name: String,
    @NotBlank(message = "color must have some value")
    val color: String,
)