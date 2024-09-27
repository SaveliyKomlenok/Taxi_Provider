package com.software.modsen.ratingservice.client;

import com.software.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${service.passenger.name}", url = "${service.passenger.rating.url}")
public interface PassengerClient {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    PassengerRatingResponse savePassengerRating(@RequestBody PassengerRatingRequest request);
}
