package com.software.modsen.authservice.client;

import com.software.modsen.authservice.config.Config;
import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.driver.name}", path = "${service.driver.url}", configuration = Config.class)
public interface DriverClient {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    DriverResponse save(@RequestBody DriverCreateRequest request);
}
