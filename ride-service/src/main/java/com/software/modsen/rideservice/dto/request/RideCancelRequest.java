package com.software.modsen.rideservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideCancelRequest {
    @Min(1)
    private Long passengerId;
    @Min(1)
    private Long rideId;
}
