package com.software.modsen.rideservice.client;

import com.software.modsen.rideservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "passenger-service", url = "localhost:8081/api/v1/passengers")
public interface PassengerClient {
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    PassengerResponse getPassengerById(@PathVariable Long id);
}
