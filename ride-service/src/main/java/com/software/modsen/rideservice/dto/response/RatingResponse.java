package com.software.modsen.rideservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse {
    private Long id;
    private Long rideId;
    private Long driverId;
    private Long passengerId;
    private double driverRating;
    private double passengerRating;
    private String comment;
}
