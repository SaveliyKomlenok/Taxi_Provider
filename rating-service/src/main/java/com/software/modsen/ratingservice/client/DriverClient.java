package com.software.modsen.ratingservice.client;

import com.software.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "driver-service", url = "localhost:8082/api/v1/driver-ratings")
public interface DriverClient {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    DriverRatingResponse saveDriverRating(@RequestBody DriverRatingRequest request);
}
