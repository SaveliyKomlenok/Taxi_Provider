package com.software.modsen.driverservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private CarResponse car;
    private String firstname;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private String gender;
    private boolean isRestricted;
}
