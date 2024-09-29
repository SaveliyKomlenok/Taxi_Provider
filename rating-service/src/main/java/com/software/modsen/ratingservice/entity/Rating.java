package com.software.modsen.ratingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ratings")
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long id;

    @Column(name = "ride_id")
    private Long rideId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "driver_rating")
    private Integer driverRating;

    @Column(name = "passenger_rating")
    private Integer passengerRating;

    @Column(name = "comment")
    private String comment;
}
