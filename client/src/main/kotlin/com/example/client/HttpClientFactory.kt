package com.example.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class HttpClientFactory {
    @Bean
    fun httpServiceProxyFactory() =
        HttpServiceProxyFactory
            .builder(
                WebClientAdapter.forClient(
                    WebClient.builder()
                        .baseUrl("http://localhost:8080")
                        .build(),
                ),
            )
            .build()

    @Bean
    fun productClient(
        httpServiceProxyFactory: HttpServiceProxyFactory,
    ) =
        httpServiceProxyFactory.createClient(ProductClient::class.java)
}
