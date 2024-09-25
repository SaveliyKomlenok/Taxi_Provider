package com.software.modsen.passengerservice.repository;

import com.software.modsen.passengerservice.entity.PassengerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {
    Optional<PassengerRating> findPassengerRatingByPassengerId(Long passengerId);
}
