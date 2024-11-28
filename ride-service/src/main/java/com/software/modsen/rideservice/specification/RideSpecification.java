package com.software.modsen.rideservice.specification;

import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class RideSpecification {
    public static Specification<Ride> hasDriverId(Long driverId) {
        return (root, query, criteriaBuilder) -> driverId == null ? null : criteriaBuilder.equal(root.get("driverId"), driverId);
    }

    public static Specification<Ride> hasPassengerId(Long passengerId) {
        return (root, query, criteriaBuilder) -> passengerId == null ? null : criteriaBuilder.equal(root.get("passengerId"), passengerId);
    }

    public static Specification<Ride> hasStatusIn(List<Status> statuses) {
        return (root, query, criteriaBuilder) -> statuses == null || statuses.isEmpty() ? null : root.get("status").in(statuses);
    }

    public static Specification<Ride> hasStartDateAfter(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) -> startDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), startDate);
    }

    public static Specification<Ride> hasEndDateBefore(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> endDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("endDateTime"), endDate);
    }
}