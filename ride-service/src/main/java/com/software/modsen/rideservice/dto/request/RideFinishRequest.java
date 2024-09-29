package com.software.modsen.rideservice.dto.request;

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
public class RideFinishRequest {
    private Long driverId;
    private Long rideId;
    private Integer passengerRating;
}
