package com.software.modsen.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Patterns {
    public static final String NAME_PATTERN = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$";
    public static final String PHONE_NUMBER_PATTERN = "^\\+375(29|33|44|25)\\d{7}$";
}
