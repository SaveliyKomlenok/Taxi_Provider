package com.software.modsen.ratingservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDriverRequest {
    private Long rideId;
    private Long driverId;
    @Range(min = 0, max = 5, message = "Incorrect rating")
    private Integer driverRating;
    private String comment;
}
