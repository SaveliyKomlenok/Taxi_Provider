package com.software.modsen.passengerservice.component.rating

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["classpath:features/passenger-rating"],
    glue = ["com/software/modsen/passengerservice/component/rating"]
)
class RatingCucumberComponentTest