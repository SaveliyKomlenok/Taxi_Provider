package com.software.modsen.authservice.dto.request;

import com.software.modsen.authservice.enumiration.Gender;
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
public class DriverCreateRequest {
    private Long car;
    private String firstname;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private Gender gender;
}
