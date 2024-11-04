package com.software.modsen.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponse {
    private String username;
    private String firstname;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
}
