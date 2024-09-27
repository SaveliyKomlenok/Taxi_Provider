package com.software.modsen.ratingservice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerRatingRequest {
    private Long passengerId;
    private double passengerRating;
}
