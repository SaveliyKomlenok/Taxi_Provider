package com.software.modsen.passengerservice.exceptionhandler

data class ValidationErrorResponse(var violations: List<Violation>)