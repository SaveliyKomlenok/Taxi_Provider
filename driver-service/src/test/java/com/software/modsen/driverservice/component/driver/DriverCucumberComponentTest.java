package com.software.modsen.driverservice.component.driver;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/driver",
        glue = "com/software/modsen/driverservice/component/driver"
)
public class DriverCucumberComponentTest {
}
