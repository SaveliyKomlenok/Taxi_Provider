package com.software.modsen.driverservice.entity;

import com.software.modsen.driverservice.enumeration.Tariff;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long id;

    @Column(name = "kind")
    private String kind;

    @Column(name = "color")
    private String color;

    @Column(name = "number")
    private String number;

    @Column(name = "tariff")
    @Enumerated(EnumType.STRING)
    private Tariff tariff;

    @Column(name = "is_restricted")
    private boolean restricted;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Driver driver;
}
