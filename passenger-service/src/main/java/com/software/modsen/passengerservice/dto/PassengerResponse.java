package com.software.modsen.passengerservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerResponse {
    private Long id;
    private String firstname;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private boolean isRestricted;
}
