package com.software.modsen.rideservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideListResponse {
    private List<RideResponse> rideResponseList;
}
