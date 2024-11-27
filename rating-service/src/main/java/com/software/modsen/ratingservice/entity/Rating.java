package com.software.modsen.ratingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Rating {
    @Id
    private String id;
    private Long rideId;
    private Long driverId;
    private Long passengerId;
    private Integer driverRating;
    private Integer passengerRating;
    private String comment;
}
