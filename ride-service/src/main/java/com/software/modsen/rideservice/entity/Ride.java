package com.software.modsen.rideservice.entity;

import com.software.modsen.rideservice.enumiration.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rides")
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "address_from")
    private String addressFrom;

    @Column(name = "address_to")
    private String addressTo;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "price")
    private BigDecimal price;
}
