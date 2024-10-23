package com.software.modsen.passengerservice.exceptionhandler

import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException
import com.software.modsen.passengerservice.exception.PassengerNotExistsException
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException
import org.springdoc.api.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(
        PassengerNotExistsException::class,
        PassengerAlreadyExistsException::class,
        PassengerRatingNotExistsException::class
    )
    fun handleErrors(exception: RuntimeException): ResponseEntity<ErrorMessage> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage(exception.message))
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onMethodArgumentNotValidException(e: MethodArgumentNotValidException): ValidationErrorResponse {
        val violations = e.bindingResult.fieldErrors.map { error ->
            Violation(error.field, error.defaultMessage!!)
        }.toList()
        return ValidationErrorResponse(violations)
    }
}