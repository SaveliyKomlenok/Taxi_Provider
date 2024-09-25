package com.software.modsen.passengerservice.repository;

import com.software.modsen.passengerservice.entity.Passenger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findAllByRestrictedIsTrue(PageRequest pageRequest);
    Optional<Passenger> findPassengerByEmailAndPhoneNumber(String email, String phoneNumber);
}
