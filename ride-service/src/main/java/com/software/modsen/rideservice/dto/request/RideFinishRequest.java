package com.software.modsen.rideservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideFinishRequest {
    private Long driverId;
    private Long rideId;
    private Integer passengerRating;
}
