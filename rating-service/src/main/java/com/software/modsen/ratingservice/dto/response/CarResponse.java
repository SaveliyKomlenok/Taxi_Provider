package com.software.modsen.ratingservice.dto.response;

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
