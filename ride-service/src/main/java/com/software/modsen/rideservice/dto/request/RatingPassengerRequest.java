package com.software.modsen.rideservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
