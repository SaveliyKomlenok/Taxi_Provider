package com.software.modsen.driverservice.entity;

import com.software.modsen.driverservice.enumeration.Tariff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("cars")
@Builder
@EqualsAndHashCode
@ToString
public class Car {
    @Id
    @Column("car_id")
    private Long id;

    @Column("kind")
    private String kind;

    @Column("color")
    private String color;

    @Column("number")
    private String number;

    @Column("tariff")
    private Tariff tariff;

    @Column("is_restricted")
    private boolean restricted;
}