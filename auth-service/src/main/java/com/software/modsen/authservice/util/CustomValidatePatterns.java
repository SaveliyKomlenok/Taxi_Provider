package com.software.modsen.authservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomValidatePatterns {
    public static final String NAME_PATTERN = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$";
    public static final String PHONE_NUMBER_PATTERN = "^\\+375(29|33|44|25)\\d{7}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9-_]{2,19}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
}
