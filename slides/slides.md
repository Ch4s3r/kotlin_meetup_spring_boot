---
layout: cover
background: https://upload.wikimedia.org/wikipedia/commons/3/37/Kotlin_Icon_2021.svg
---

# Welcome @ Kotlin meetup

---
layout: two-cols
---

# About me


<v-clicks>

Via Ferrata fan

Loves cats

<img src="cat.png" class="h-60 rounded shadow" />

Likes low level languages like Rust

</v-clicks>

::right::

<v-clicks>

Pizza lover / Hobby pizza maker

<img src="pizza.png" class="h-50 rounded shadow" />


Working at and sponsored by

<img src="george.png" class="h-30 rounded shadow" />

Age at george: ~4 years

</v-clicks>

---

# Spring Boot 3.X

<v-clicks>

requires Java 17 or later

Also tested against Java 19

build on top of Spring Framework 6

</v-clicks>

---
layout: center
---

# Migration guide from 2.7 to 3.0

---

# Deprecation

Deprecations in Spring Boot 2.x have been removed in this release

---

# Application properties migration

<v-clicks>

Few properties got renamed/removed

For an easy migration path use this:

```kotlin
runtime("org.springframework.boot:spring-boot-properties-migrator")
```

Don’t forget to remove the dependency after migration is done ;)

</v-clicks>

<!--
[click]

A few configuration properties were renamed/removed and developers need to update their application.properties/application.yaml accordingly.

[click]
[click]

To help you with that, Spring Boot provides a spring-boot-properties-migrator module. Once added as a dependency to your project, this will not only analyze your application’s environment and print diagnostics at startup, but also temporarily migrate properties at runtime for you.

[click]

Once you’re done with the migration, please make sure to remove this module from your project’s dependencies.
-->

---

# Logging Date Format

<v-clicks>

Default logging date changed  
Now uses ISO-8601

New: yyyy-MM-dd’T’HH:mm:ss.SSSXXX  
Old: yyyy-MM-dd HH:mm:ss.SSS

Env: LOG_DATEFORMAT_PATTERN  
Property: logging.pattern.dateformat

</v-clicks>

<!--
[click]

Logback and Log4j2 changed default logging date

[click]

T separates the date and time instead of a space character
timezone offset at the end

[click]

Can be restored with these properties.
-->

---

# Configuration properties

<v-clicks>

Extra annotation @ConstructingBinding is no longer needed at the type level

Now you can use @ConfigurationProperties without this additional annotation.

Can be removed from most classes.

Exception: multiple constructors

</v-clicks>

<!--
When a class or record has multiple constructors, it may still be used on a constructor to indicate which one should be used for property binding.
-->

<v-click>

```kotlin{all|1,3-10}
@ConfigurationProperties("product")
@ConstructorBinding
data class ServiceProperties(
    val devEnvironment: Boolean = false,
)
```

</v-click>

<v-click>

application.properties
```properties
product.devEnvironment=true
```

</v-click>

---

# Configuration properties dependencies

<v-clicks>

Kotlin Annotation Processor

build.gradle.kts
```kotlin
plugins {
    kotlin("kapt") version "1.7.22"
}
```

```kotlin
dependencies {
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}
```

Kapt is in maintenance mode

Still gets security updates

Hopefully spring will support KSP in the future

</v-clicks>

---

# spring.factories is no more

<v-clicks>

Was used to load Autoconfigurations

```kotlin
@Configuration
class MyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun myService(): MyService { ... }
}
```

spring.factories
```properties
org.springframework.boot.autoconfigure.AutoConfigurationImportListener=\
    com.example.MyAutoConfiguration
```

Replacement:

```
src/main/reosources/META-INF/
spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```
</v-clicks>

---

# URL route matching

<v-clicks>

Trailing slash will not be automatically handled by spring

```kotlin
@GetMapping("/product")
```

```kotlin
@GetMapping("/product/")
```

Now only matches /product and not /product/ anymore

</v-clicks>

---

# URL route matching

Possible solutions:

<v-clicks>

```kotlin
@GetMapping("/some/greeting", "/some/greeting/")
```

```kotlin{6}
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
      configurer.setUseTrailingSlashMatch(true);
    }
}
```

</v-clicks>

---
layout: center
---

# Existing features pre 3.X

---

# Coroutines

<v-clicks>

What are coroutines?

Lightweight / green threads that are quite cheap to spawn in comparison to java threads

Used for asynchrounously (interleaved) executing jobs not parallel (at the same time).

</v-clicks>


---

# Coroutines example 1/1

<v-clicks>

```kotlin
suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}
```

```kotlin
val time = measureTimeMillis {
    val one = doSomethingUsefulOne()
    val two = doSomethingUsefulTwo()
    println("The answer is ${one + two}")
}
println("Completed in $time ms")
```

Guesses?

```kotlin
The answer is 42
Completed in 2017 ms
```
</v-clicks>

---

# Coroutines example 2/1

<v-clicks>

```kotlin
val time = measureTimeMillis {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```


Guesses again?

```kotlin
The answer is 42
Completed in 1017 ms
```

```kotlin{2-3}
val time = measureTimeMillis {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    println("The answer is ${one.await() + two.await()}")
}
println("Completed in $time ms")
```

</v-clicks>


---

# Coroutine support in Spring Data

<v-clicks>

Spring Data R2DBC

```kotlin{all|2|5-9}
@Repository
interface ProductRepository : CoroutineCrudRepository<Product, Long> {

    // Some examples not used in this presentation
    suspend fun findProductById(id: Long): Product

    fun findByName(name: String): Flow<Product>

    suspend fun findAllByName(name: String): List<Product>
}
```

</v-clicks>

---

# Coroutine support in Spring Webflux/MVC

<v-clicks>

Spring Webflux

```kotlin{all|8|11}
@RestController
@RequestMapping("/product")
class ProductController(
    val productService: ProductService,
) {

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: Long) = productService.findById(id)
    
    @GetMapping
    fun findAll() = productService.findAll()
}

```

</v-clicks>


---
layout: center
---

# New features

---

# Http client

<v-clicks>

```kotlin{all|1|4|all}
@HttpExchange("/product")
interface ProductClient {
    
    @GetExchange("/{id}")
    suspend fun findById(
        @PathVariable id: Long,
    ): Product?
}
```

```kotlin{all|4-6|8-12}
@Configuration
class HttpClientFactory {

    @Bean
    fun productClient(httpServiceProxyFactory: HttpServiceProxyFactory) =
        httpServiceProxyFactory.createClient(ProductClient::class.java)
        
    @Bean
    fun httpServiceProxyFactory() =
        HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(WebClient.builder().baseUrl("http://localhost:8080").build()))
            .build()
}
```

</v-clicks>

---

# GraalVM support

<v-clicks>

Convert service to native image

Fast startup

Less memory usage

Ahead of time compilation

Most spring boot apps work out of the box

</v-clicks>

<!--

Spring applications can be converted into native images

[click]
[click]


This can provide significant memory and startup-up performance improvement

[click]

As everything that is needed and analyzed at runtime will be done at compile time.

So there is a longer compile time but super fast startup and a lot less memory usage.

[click]

Most spring boot services by should work out of the box with native image.

And those that are not working you can give the compiler a little hint which reflections is going on.

Last Kotlin meetup there was a talk about GraalVM, if you're interested check it out.

-->

---

# @SpringBootTest with Main Methods


<v-clicks>

```kotlin
fun main(args: Array<String>) {
    // custom logic with was not testable before
    runApplication<Application>(*args)
}
```

```kotlin
@SpringBootTest(useMainMethod=ALWAYS)
class IntegrationTest { ... }
```

</v-clicks>

<!--
The @SpringBootTest annotation can now use the main of any discovered @SpringBootConfiguration class if it’s available. This means that any custom SpringApplication configuration performed by your main method can now be picked up by tests.
To use the main method for a test set the useMainMethod attribute of @SpringBootTest to UseMainMethod.ALWAYS or UseMainMethod.WHEN_AVAILABLE.
-->

---

# Metrics

<v-clicks>

Spring Cloud Sleuth allows you to aggregate and track log entries as requests move through a distributed software system

Spring cloud sleuth got removed and replaced with micrometer

</v-clicks>

---

# Debugging reactive context

It's finally possible to evaluate expressions in Intellij when using webflux and Kotlin coroutines 🎉

---
layout: center
---

# Demo

---

# Problem+json


<v-clicks>

rfc7807 - Problem Details for HTTP APIs

Why?

To standardize error messages.

```http request
HTTP/1.1 400 Bad Request
Content-Length: 118
Content-Type: application/problem+json
```
```json
{
    "detail": "Failed to read HTTP message",
    "instance": "/product",
    "status": 400,
    "title": "Bad Request",
    "type": "about:blank"
}

```

</v-clicks>

<!--
https://www.sivalabs.in/spring-boot-3-error-reporting-using-problem-details/
-->

---
layout: center
---

# Demo

---

# Validation gotchas

<v-clicks>

```kotlin
    @PostMapping
    suspend fun save(
        @Valid @RequestBody
        product: Product,
    ) = ...
```

```kotlin
data class Product(
    @Id
    val id: Long = 0,
    @Size(min = 5)
    val name: String,
    @NotBlank(message = "color must have some value")
    val color: String,
)
```

Guesses?


</v-clicks>

---

# Validation gotchas solution

```kotlin{all|4,6}
data class Product(
    @Id
    val id: Long = 0,
    @field:Size(min = 5)
    val name: String,
    @field:NotBlank(message = "color must have some value")
    val color: String,
)
```

---

# Demo


<!--
//    https://blog.jdriven.com/2020/10/kotlin-and-spring-validation/
data class Product(
    @Id
    val id: Long = 0,
    @field:Size(min = 5)
    val name: String,
    @field:NotBlank(message = "color must have some value")
    val color: String?,
)
-->

