package com.modsen.software.endtoendtesting.dto.request;

import com.modsen.software.endtoendtesting.enumiration.Tariff;
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
public class CarCreateRequest {
    private String kind;
    private String color;
    private String number;
    private Tariff tariff;
}
