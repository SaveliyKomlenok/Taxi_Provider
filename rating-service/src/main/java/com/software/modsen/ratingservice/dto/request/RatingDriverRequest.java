package com.software.modsen.ratingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDriverRequest {
    private Long rideId;
    private Long driverId;
    private Long passengerId;
    @Range(min = 0, max = 5, message = "Incorrect rating")
    private Integer driverRating;
    @NotBlank(message = "Incorrect comment")
    private String comment;
}
