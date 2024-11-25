package com.software.modsen.driverservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("driver_ratings")
@Builder
@EqualsAndHashCode
@ToString
public class DriverRating {
    @Id
    @Column("driver_rating_id")
    private Long id;

    @Column("rating")
    private double rating;

    @Column("driver_id")
    private Long driverId;

    @Column("version")
    private Long version;

    @Transient
    private Driver driver;
}