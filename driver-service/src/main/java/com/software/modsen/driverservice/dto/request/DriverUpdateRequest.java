package com.software.modsen.driverservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.software.modsen.driverservice.util.CustomValidatePatterns.NAME_PATTERN;
import static com.software.modsen.driverservice.util.CustomValidatePatterns.PHONE_NUMBER_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverUpdateRequest {
    private Long car;

    @Pattern(regexp = NAME_PATTERN, message = "Incorrect firstname")
    @Schema(defaultValue = "firstname")
    private String firstname;

    @Pattern(regexp = NAME_PATTERN, message = "Incorrect surname")
    @Schema(defaultValue = "surname")
    private String surname;

    @Pattern(regexp = NAME_PATTERN, message = "Incorrect patronymic")
    @Schema(defaultValue = "patronymic")
    private String patronymic;

    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "Incorrect phone number")
    @Schema(defaultValue = "phone number")
    private String phoneNumber;
}
