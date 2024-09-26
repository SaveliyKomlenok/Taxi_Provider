package com.software.modsen.passengerservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static com.software.modsen.passengerservice.util.Patterns.NAME_PATTERN;
import static com.software.modsen.passengerservice.util.Patterns.PHONE_NUMBER_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerUpdateRequest {
    private Long id;

    @Pattern(regexp = NAME_PATTERN, message = "Incorrect firstname")
    @Schema(defaultValue = "firstname")
    private String firstname;

    @Pattern(regexp = NAME_PATTERN,message = "Incorrect surname")
    @Schema(defaultValue = "surname")
    private String surname;

    @Pattern(regexp = NAME_PATTERN,message = "Incorrect patronymic")
    @Schema(defaultValue = "patronymic")
    private String patronymic;

    @Email(message = "Incorrect email")
    @Schema(defaultValue = "email")
    private String email;

    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "Incorrect phone number")
    @Schema(defaultValue = "phone number")
    private String phoneNumber;
}
