package com.software.modsen.passengerservice.dto.request;

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
