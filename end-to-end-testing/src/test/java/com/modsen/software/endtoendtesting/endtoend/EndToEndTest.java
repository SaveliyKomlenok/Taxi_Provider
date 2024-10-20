package com.modsen.software.endtoendtesting.endtoend;

import com.modsen.software.endtoendtesting.dto.response.CarResponse;
import com.modsen.software.endtoendtesting.dto.response.DriverResponse;
import com.modsen.software.endtoendtesting.dto.response.PassengerResponse;
import com.modsen.software.endtoendtesting.dto.response.RideResponse;
import com.modsen.software.endtoendtesting.enumiration.Status;
import com.modsen.software.endtoendtesting.util.EndToEndTestEntities;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EndToEndTest {

    private static <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, request, responseType);
    }

    private static ResponseEntity<PassengerResponse> createPassenger() {
        return postForEntity("http://localhost:8765/api/v1/passengers",
                EndToEndTestEntities.getPassengerCreateRequest(),
                PassengerResponse.class);
    }

    private static ResponseEntity<CarResponse> createCar() {
        return postForEntity("http://localhost:8765/api/v1/cars",
                EndToEndTestEntities.getCarCreateRequest(),
                CarResponse.class);
    }

    private static ResponseEntity<DriverResponse> createDriver(CarResponse car) {
        return postForEntity("http://localhost:8765/api/v1/drivers",
                EndToEndTestEntities.getDriverCreateRequest(car),
                DriverResponse.class);
    }

    private static ResponseEntity<RideResponse> createRide(PassengerResponse passenger) {
        return postForEntity("http://localhost:8765/api/v1/rides",
                EndToEndTestEntities.getRideCreateRequest(passenger),
                RideResponse.class);
    }

    private static ResponseEntity<RideResponse> acceptRide(RideResponse ride, DriverResponse driver) {
        return postForEntity("http://localhost:8765/api/v1/rides/accept",
                EndToEndTestEntities.getRideStatusChangeRequest(ride, driver),
                RideResponse.class);
    }

    private static ResponseEntity<RideResponse> changeRideStatus(RideResponse ride, DriverResponse driver) {
        return postForEntity("http://localhost:8765/api/v1/rides/change-status",
                EndToEndTestEntities.getRideStatusChangeRequest(ride, driver),
                RideResponse.class);
    }

    private static ResponseEntity<RideResponse> finishRide(RideResponse ride, DriverResponse driver) {
        return postForEntity("http://localhost:8765/api/v1/rides/finish",
                EndToEndTestEntities.getRideFinishRequest(ride, driver),
                RideResponse.class);
    }

    @Test
    void testRideEndToEnd() {
        ResponseEntity<PassengerResponse> passengerResponse = createPassenger();
        ResponseEntity<RideResponse> rideResponse = createRide(passengerResponse.getBody());

        ResponseEntity<CarResponse> carResponse = createCar();
        ResponseEntity<DriverResponse> driverResponse = createDriver(carResponse.getBody());

        acceptRide(rideResponse.getBody(), driverResponse.getBody());
        changeRideStatus(rideResponse.getBody(), driverResponse.getBody());
        changeRideStatus(rideResponse.getBody(), driverResponse.getBody());
        ResponseEntity<RideResponse> finishedRideResponse = finishRide(rideResponse.getBody(), driverResponse.getBody());

        assertThat(finishedRideResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requireNonNull(finishedRideResponse.getBody()).getPassengerId())
                .isEqualTo(requireNonNull(passengerResponse.getBody()).getId());
        assertThat(requireNonNull(finishedRideResponse.getBody()).getDriverId())
                .isEqualTo(requireNonNull(driverResponse.getBody()).getId());
        assertThat(requireNonNull(finishedRideResponse.getBody()).getStatus()).isEqualTo(Status.FINISHED);
    }
}
