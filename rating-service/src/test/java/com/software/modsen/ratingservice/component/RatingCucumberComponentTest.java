package com.software.modsen.ratingservice.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "com/software/modsen/ratingservice/component"
)
public class RatingCucumberComponentTest {
}
