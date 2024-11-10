package com.software.modsen.authservice.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.software.modsen.authservice.util.CustomValidatePatterns.NAME_PATTERN;
import static com.software.modsen.authservice.util.CustomValidatePatterns.PASSWORD_PATTERN;
import static com.software.modsen.authservice.util.CustomValidatePatterns.PHONE_NUMBER_PATTERN;
import static com.software.modsen.authservice.util.CustomValidatePatterns.USERNAME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRegistrationRequest {
    @Pattern(regexp = USERNAME_PATTERN, message = "Incorrect username")
    private String username;

    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect password")
    private String password;

    @Pattern(regexp = NAME_PATTERN, message = "Incorrect firstname")
    private String firstname;

    @Pattern(regexp = NAME_PATTERN,message = "Incorrect surname")
    private String surname;

    @Pattern(regexp = NAME_PATTERN,message = "Incorrect patronymic")
    private String patronymic;

    @Email(message = "Incorrect email")
    private String email;

    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "Incorrect phone number")
    private String phoneNumber;
}
