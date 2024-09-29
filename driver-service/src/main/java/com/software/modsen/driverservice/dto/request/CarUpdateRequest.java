package com.software.modsen.driverservice.dto.request;

import com.software.modsen.driverservice.enumeration.Tariff;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.software.modsen.driverservice.util.CustomValidatePatterns.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarUpdateRequest {
    private Long id;

    @Pattern(regexp = CAR_KIND_PATTERN, message = "Incorrect kind")
    @Schema(defaultValue = "kind")
    private String kind;

    @Pattern(regexp = CAR_COLOR_PATTERN, message = "Incorrect color")
    @Schema(defaultValue = "color")
    private String color;

    @Pattern(regexp = CAR_NUMBER_PATTERN, message = "Incorrect number")
    @Schema(defaultValue = "number")
    private String number;

    private Tariff tariff;
}
