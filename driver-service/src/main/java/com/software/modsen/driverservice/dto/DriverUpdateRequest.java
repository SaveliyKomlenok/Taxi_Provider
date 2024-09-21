package com.software.modsen.driverservice.dto;

import com.software.modsen.driverservice.enumeration.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverUpdateRequest {
    private Long id;
    private Long car;

    @Pattern(regexp = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$", message = "Incorrect firstname")
    @Schema(defaultValue = "firstname")
    private String firstname;

    @Pattern(regexp = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$", message = "Incorrect surname")
    @Schema(defaultValue = "surname")
    private String surname;

    @Pattern(regexp = "^[а-яА-Я]{2,}\\s?-?[а-яА-Я]{2,}$", message = "Incorrect patronymic")
    @Schema(defaultValue = "patronymic")
    private String patronymic;

    @Email(message = "Incorrect email")
    @Schema(defaultValue = "email")
    private String email;

    @Pattern(regexp = "^\\+375(29|33|44|25)\\d{7}$", message = "Incorrect phone number")
    @Schema(defaultValue = "phone number")
    private String phoneNumber;

    private Gender gender;
}
