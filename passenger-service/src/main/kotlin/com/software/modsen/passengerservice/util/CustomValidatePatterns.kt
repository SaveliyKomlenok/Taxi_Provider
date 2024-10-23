package com.software.modsen.passengerservice.util

object CustomValidatePatterns {
    const val NAME_PATTERN = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$"
    const val PHONE_NUMBER_PATTERN = "^\\+375(29|33|44|25)\\d{7}$"
}