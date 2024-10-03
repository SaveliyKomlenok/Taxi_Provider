package com.software.modsen.rideservice.client;

import com.software.modsen.rideservice.config.FeignConfig;
import com.software.modsen.rideservice.dto.request.RatingPassengerRequest;
import com.software.modsen.rideservice.dto.response.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.rating.name}", path = "${service.rating.url}", configuration = FeignConfig.class)
public interface RatingClient {
    @RequestMapping(method = RequestMethod.POST, value = "/passenger", produces = "application/json")
    RatingResponse rateByDriver(@RequestBody RatingPassengerRequest request);
}
