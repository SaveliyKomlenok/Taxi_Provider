package com.software.modsen.rideservice.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class RideCreateRequest {
    @NotBlank(message = "Incorrect address from where to leave")
    private String addressFrom;
    @NotBlank(message = "Incorrect address where to go")
    private String addressTo;
}
