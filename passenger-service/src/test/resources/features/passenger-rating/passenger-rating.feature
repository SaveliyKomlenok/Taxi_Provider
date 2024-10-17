Feature: Passenger rating
  Scenario: Get passenger rating by id
    Given A passenger rating with id 1 exists
    When I request the passenger rating with id 1 from getById method
    Then I should receive the passenger rating details with id 1

  Scenario: Get passenger rating by id that not exist
    Given A passenger rating exists with id 99 does not exist
    When I request the passenger rating with id 99 from getById method
    Then I should receive a PassengerRatingNotExistsException with id 99

  Scenario: Save a new passenger rating successfully
    Given A passenger rating with id 1 does not exists and saved
    When I save passenger rating
    Then The new passenger rating should be saved successfully

  Scenario: Save a existing passenger rating successfully
    Given A passenger rating with id 1 exists and saved
    When I save passenger rating
    Then The existing passenger rating should be saved successfully