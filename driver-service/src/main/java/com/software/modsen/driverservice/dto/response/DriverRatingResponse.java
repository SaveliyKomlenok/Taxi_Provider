package com.software.modsen.driverservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRatingResponse {
    private Long id;
    private DriverResponse driver;
    private double driverRating;
}
