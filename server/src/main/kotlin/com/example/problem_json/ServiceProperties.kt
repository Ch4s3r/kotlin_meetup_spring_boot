package com.example.problem_json

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(ServiceProperties::class)
@Configuration
class ServicePropertiesConfiguration

@ConfigurationProperties("product")
data class ServiceProperties(
    val devEnvironment: Boolean = false,
)
