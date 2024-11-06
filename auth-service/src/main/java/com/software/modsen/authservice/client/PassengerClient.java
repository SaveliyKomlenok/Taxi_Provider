package com.software.modsen.authservice.client;

import com.software.modsen.authservice.config.FeignClientConfig;
import com.software.modsen.authservice.dto.request.PassengerCreateRequest;
import com.software.modsen.authservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.passenger.name}", path = "${service.passenger.url}", configuration = FeignClientConfig.class)
public interface PassengerClient {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    PassengerResponse save(@RequestBody PassengerCreateRequest request);
}
