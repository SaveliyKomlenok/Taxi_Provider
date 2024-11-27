package com.software.modsen.ratingservice.dto.response;

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
public class RatingResponse {
    private String id;
    private Long rideId;
    private Long driverId;
    private Long passengerId;
    private double driverRating;
    private double passengerRating;
    private String comment;
}
