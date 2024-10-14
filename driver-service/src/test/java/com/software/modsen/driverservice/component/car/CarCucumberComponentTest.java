package com.software.modsen.driverservice.component.car;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/car",
        glue = "com/software/modsen/driverservice/component/car"
)
public class CarCucumberComponentTest {
}
