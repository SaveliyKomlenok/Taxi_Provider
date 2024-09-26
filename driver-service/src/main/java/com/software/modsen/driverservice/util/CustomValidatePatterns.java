package com.software.modsen.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomValidatePatterns {
    public static final String CAR_KIND_PATTERN = "^[a-zA-Z]+\\s?-?[a-zA-Z]+$";
    public static final String CAR_NUMBER_PATTERN = "^[a-zA-Z0-9]{4,8}$";
    public static final String CAR_COLOR_PATTERN = "^[a-zA-Z]+-?[a-zA-Z]+$";
    public static final String NAME_PATTERN = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$";
    public static final String PHONE_NUMBER_PATTERN = "^\\+375(29|33|44|25)\\d{7}$";
}
