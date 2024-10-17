Feature: Driver rating
  Scenario: Get driver rating by id
    Given A driver rating with id 1 exists
    When I request the driver rating with id 1 from getById method
    Then I should receive the driver rating details with id 1

  Scenario: Get driver rating by id that not exist
    Given A driver rating exists with id 99 does not exist
    When I request the driver rating with id 99 from getById method
    Then I should receive a DriverRatingNotExistsException with id 99

  Scenario: Save a new driver rating successfully
    Given A driver rating with id 1 does not exist and saved
    When I save driver rating
    Then The new driver rating should be saved successfully

  Scenario: Save a existing driver rating successfully
    Given A driver rating with id 1 exists and saved
    When I save driver rating
    Then The existing driver rating should be saved successfully