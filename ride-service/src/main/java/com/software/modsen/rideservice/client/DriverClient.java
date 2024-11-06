package com.software.modsen.rideservice.client;

import com.software.modsen.rideservice.config.FeignConfig;
import com.software.modsen.rideservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.driver.name}", path = "${service.driver.url}", configuration = FeignConfig.class)
public interface DriverClient {
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    DriverResponse getDriverById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/busy", produces = "application/json")
    DriverResponse changeBusyStatus(@RequestBody DriverChangeStatusRequest request);
}
