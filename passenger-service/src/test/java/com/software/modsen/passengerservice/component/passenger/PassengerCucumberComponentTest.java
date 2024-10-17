package com.software.modsen.passengerservice.component.passenger;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/passenger",
        glue = "com/software/modsen/passengerservice/component/passenger"
)
public class PassengerCucumberComponentTest {
}
