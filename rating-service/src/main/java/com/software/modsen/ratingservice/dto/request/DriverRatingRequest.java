package com.software.modsen.ratingservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRatingRequest {
    private Long driverId;
    private double driverRating;
}
