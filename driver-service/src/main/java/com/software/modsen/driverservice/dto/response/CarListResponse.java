package com.software.modsen.driverservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarListResponse {
    List<CarResponse> carResponseList;
}
