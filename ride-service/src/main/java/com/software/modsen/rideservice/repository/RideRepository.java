package com.software.modsen.rideservice.repository;

import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findRidesByPassengerIdAndStatusEquals(Long passengerId, Status status);

    List<Ride> findRidesByDriverIdAndStatusEquals(Long passengerId, Status status);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Ride> findRideByIdAndStatusEquals(Long id, Status status);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Ride> findRideByIdAndDriverIdAndStatusEquals(Long id, Long driverId, Status status);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Ride> findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(Long id, Long driverId, Status firstStatus, Status secondStatus);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Ride> findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(Long id, Long driverId, Status firstStatus, Status secondStatus);
}
