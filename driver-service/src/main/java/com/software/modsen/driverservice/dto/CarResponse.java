package com.software.modsen.driverservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponse {
    private Long id;
    private String kind;
    private String color;
    private String number;
    private String tariff;
    private boolean isRestricted;
}
