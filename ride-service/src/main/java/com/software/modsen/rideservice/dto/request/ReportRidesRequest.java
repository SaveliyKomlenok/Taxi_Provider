package com.software.modsen.rideservice.dto.request;

import com.software.modsen.rideservice.enumiration.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportRidesRequest {
    @NotBlank(message = "Email receiver must not be empty")
    @Email(message = "Email should be valid")
    private String emailReceiver;
    private Long driverId;
    private Long passengerId;
    private List<Status> statuses;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
