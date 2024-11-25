package com.software.modsen.driverservice.entity;

import com.software.modsen.driverservice.enumeration.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("drivers")
@Builder
@EqualsAndHashCode
@ToString
public class Driver {
    @Id
    @Column("driver_id")
    private Long id;

    @Column("car_id")
    private Long carId;

    @Column("firstname")
    private String firstname;

    @Column("surname")
    private String surname;

    @Column("patronymic")
    private String patronymic;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("gender")
    private Gender gender;

    @Column("is_restricted")
    private boolean restricted;

    @Column("is_busy")
    private boolean busy;

    @Transient
    private Car car;
}