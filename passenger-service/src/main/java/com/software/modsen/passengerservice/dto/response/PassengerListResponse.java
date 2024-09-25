package com.software.modsen.passengerservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerListResponse {
    List<PassengerResponse> passengerResponseList;
}
