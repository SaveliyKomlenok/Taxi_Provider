package com.software.modsen.authservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthParameters {
    public static final String AUTH_HEADER = "Authorization";
    public static final Integer USER = 0;
    public static final String USER_SEARCH_PARAM = "?username=";
    public static final String USER_ID_ATTRIBUTE = "userId";
    public static final String AUTH_TYPE = "Bearer ";
}
