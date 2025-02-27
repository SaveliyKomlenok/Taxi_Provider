package com.software.modsen.rideservice.exceptionhandler;

import com.software.modsen.rideservice.exception.DriverBusyException;
import com.software.modsen.rideservice.exception.DriverRestrictedException;
import com.software.modsen.rideservice.exception.FeignClientException;
import com.software.modsen.rideservice.exception.NotFoundException;
import com.software.modsen.rideservice.exception.PassengerRestrictedException;
import com.software.modsen.rideservice.exception.RideAcceptException;
import com.software.modsen.rideservice.exception.RideCancelException;
import com.software.modsen.rideservice.exception.RideChangeStatusException;
import com.software.modsen.rideservice.exception.RideFinishException;
import com.software.modsen.rideservice.exception.RideNotExistsException;
import com.software.modsen.rideservice.exception.ServiceNotAvailableException;
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
    @ExceptionHandler({RideNotExistsException.class,
            DriverBusyException.class,
            DriverRestrictedException.class,
            FeignClientException.class})
    public ResponseEntity<ErrorMessage> handleBadRequestErrors(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler({PassengerRestrictedException.class,
            RideAcceptException.class,
            RideCancelException.class,
            RideChangeStatusException.class,
            RideFinishException.class})
    public ResponseEntity<ErrorMessage> handleConflictErrors(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundErrors(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ServiceNotAvailableException.class)
    public ResponseEntity<ErrorMessage> handleServiceUnavailableErrors(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
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