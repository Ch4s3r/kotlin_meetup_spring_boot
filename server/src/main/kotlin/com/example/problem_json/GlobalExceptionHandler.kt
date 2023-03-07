package com.example.problem_json

import org.springframework.context.annotation.Configuration
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebInputException

// https://www.rfc-editor.org/rfc/rfc7807
//@ControllerAdvice
class GlobalExceptionHandler(
    val serviceProperties: ServiceProperties,
) {

    @ExceptionHandler
    fun handleException(throwable: Throwable) =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(500)).apply {
            title = "Throwable"
            detail = if (serviceProperties.devEnvironment) throwable.stackTraceToString() else null
        }

    @ExceptionHandler
    fun handleException(throwable: NotFoundException) =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(404)).apply {
            title = "Not found"
        }
    // return NotFoundException
}

//@RestControllerAdvice
class GlobalCustomExceptionHandler {
    @ExceptionHandler
    fun handleException(throwable: ServerWebInputException) =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(444)).apply {
            title = "Validation Exception"
            setProperty("errors", throwable.detailMessageArguments)
        }
    @ExceptionHandler
    fun handleException(throwable: Throwable) =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(500)).apply {
            title = "Throwable"
        }
}
