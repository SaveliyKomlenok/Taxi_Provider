package com.software.modsen.driverservice.dto.request;

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
