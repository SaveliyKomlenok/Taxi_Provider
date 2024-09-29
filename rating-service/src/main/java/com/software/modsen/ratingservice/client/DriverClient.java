package com.software.modsen.ratingservice.client;

import com.software.modsen.ratingservice.config.FeignConfig;
import com.software.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.driver.name}", url = "${service.driver.rating.url}", configuration = FeignConfig.class)
public interface DriverClient {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    DriverRatingResponse saveDriverRating(@RequestBody DriverRatingRequest request);
}
