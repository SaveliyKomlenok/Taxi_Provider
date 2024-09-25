package com.software.modsen.passengerservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerRatingResponse {
    private Long id;
    private PassengerResponse passenger;
    private double passengerRating;
}
