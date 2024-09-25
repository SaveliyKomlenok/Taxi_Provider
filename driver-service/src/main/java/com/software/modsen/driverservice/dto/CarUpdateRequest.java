package com.software.modsen.driverservice.dto;

import com.software.modsen.driverservice.enumeration.Tariff;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarUpdateRequest {
    private Long id;

    @Pattern(regexp = "^[a-zA-Z]+\\s?-?[a-zA-Z]+$", message = "Incorrect kind")
    @Schema(defaultValue = "kind")
    private String kind;

    @Pattern(regexp = "^[a-zA-Z]+-?[a-zA-Z]+$", message = "Incorrect color")
    @Schema(defaultValue = "color")
    private String color;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,8}$", message = "Incorrect number")
    @Schema(defaultValue = "number")
    private String number;

    private Tariff tariff;
}
