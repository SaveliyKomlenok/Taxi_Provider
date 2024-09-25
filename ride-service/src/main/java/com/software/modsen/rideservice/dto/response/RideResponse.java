package com.software.modsen.rideservice.dto.response;

import com.software.modsen.rideservice.enumiration.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideResponse {
    private Long id;
    private Long driverId;
    private Long passengerId;
    private String addressFrom;
    private String addressTo;
    private Status status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private double price;
}
