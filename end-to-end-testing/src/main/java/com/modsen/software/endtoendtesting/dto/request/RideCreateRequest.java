package com.modsen.software.endtoendtesting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideCreateRequest {
    private Long passengerId;
    private String addressFrom;
    private String addressTo;
}
