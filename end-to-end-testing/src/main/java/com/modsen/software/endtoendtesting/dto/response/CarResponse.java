package com.modsen.software.endtoendtesting.dto.response;

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
public class CarResponse {
    private Long id;
    private String kind;
    private String color;
    private String number;
    private String tariff;
    private boolean isRestricted;
}
