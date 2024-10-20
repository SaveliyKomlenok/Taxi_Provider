package com.modsen.software.endtoendtesting.dto.request;

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
public class PassengerCreateRequest {
    private String firstname;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
}
