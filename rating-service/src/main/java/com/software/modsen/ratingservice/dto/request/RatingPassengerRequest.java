package com.software.modsen.ratingservice.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingPassengerRequest {
    private Long rideId;
    private Long driverId;
    private Long passengerId;
    @Range(min = 0, max = 5, message = "Incorrect rating")
    private Integer passengerRating;
}
