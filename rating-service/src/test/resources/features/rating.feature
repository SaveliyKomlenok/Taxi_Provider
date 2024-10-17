Feature: Rating
  Scenario: Rate a passenger successfully
    Given I have a valid passenger rating
    When I save the passenger rating
    Then I should receive the saved passenger rating

  Scenario: Rate a driver successfully
    Given I have a valid driver rating request
    And the driver has not been rated yet
    When I save the driver rating
    Then I should receive the updated driver rating

  Scenario: Attempt to rate a driver that has already been rated
    Given I have a valid driver rating request
    And the driver has already been rated
    When I save the driver rating
    Then I should receive a DriverAlreadyHasRatingException

  Scenario: Attempt to rate a driver when the rating is not found
    Given I have a valid driver rating request
    And the rating does not exist
    When I save the driver rating
    Then I should receive a RatingDriverException