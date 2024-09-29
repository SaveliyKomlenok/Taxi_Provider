package com.software.modsen.rideservice.client;

import com.software.modsen.rideservice.config.FeignConfig;
import com.software.modsen.rideservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.passenger.name}", url = "${service.passenger.url}", configuration = FeignConfig.class)
public interface PassengerClient {
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    PassengerResponse getPassengerById(@PathVariable Long id);
}
