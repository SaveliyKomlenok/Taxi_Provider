package com.software.modsen.rideservice.client;

import com.software.modsen.rideservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.driver.name}", url = "${service.driver.url}")
public interface DriverClient {
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    DriverResponse getDriverById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/busy/{id}", produces = "application/json")
    DriverResponse changeBusyStatus(@PathVariable Long id);
}
