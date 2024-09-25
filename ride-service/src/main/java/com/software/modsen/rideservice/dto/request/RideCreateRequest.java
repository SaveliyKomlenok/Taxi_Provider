package com.software.modsen.rideservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideCreateRequest {
    private Long passengerId;
    @NotBlank(message = "Incorrect address from where to leave")
    private String addressFrom;
    @NotBlank(message = "Incorrect address where to go")
    private String addressTo;
}
