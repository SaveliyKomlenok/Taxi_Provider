package com.software.modsen.ratingservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRatingResponse {
    private Long driverId;
    private double driverRating;
}
