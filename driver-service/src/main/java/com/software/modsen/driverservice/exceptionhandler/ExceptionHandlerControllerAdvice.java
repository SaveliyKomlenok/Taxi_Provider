package com.software.modsen.driverservice.exceptionhandler;

import com.software.modsen.driverservice.exception.CarAlreadyExistsException;
import com.software.modsen.driverservice.exception.CarNotExistsException;
import com.software.modsen.driverservice.exception.CarOccupiedException;
import com.software.modsen.driverservice.exception.DriverAlreadyExistsException;
import com.software.modsen.driverservice.exception.DriverNotExistsException;
import com.software.modsen.driverservice.exception.DriverRatingNotExistsException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler({CarNotExistsException.class,
            DriverNotExistsException.class,
            CarAlreadyExistsException.class,
            DriverAlreadyExistsException.class,
            DriverRatingNotExistsException.class})
    public ResponseEntity<ErrorMessage> handleBadRequestErrors(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(CarOccupiedException.class)
    public ResponseEntity<ErrorMessage> handleConflictErrors(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}