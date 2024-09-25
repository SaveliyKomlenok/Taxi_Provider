package com.software.modsen.ratingservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerRatingResponse {
    private Long passengerId;
    private double passengerRating;
}
