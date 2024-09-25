package com.software.modsen.rideservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideStatusChangeRequest {
    private Long driverId;
    private Long rideId;
}
