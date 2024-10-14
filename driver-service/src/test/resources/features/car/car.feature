Feature: Car
  Scenario: Get car by id
    Given A car with id 1 exists
    When I request the car with id 1 from getById method
    Then I should receive the car details with id 1

  Scenario: Get car by id that not exist
    Given A car exists with id 99 does not exist
    When I request the car with id 99 from getById method
    Then I should receive a CarNotExistsException with id 99

  Scenario: Save a new car successfully
    Given A car with number "ABC1234" does not exist
    When I save the car
    Then The car should be saved successfully

  Scenario: Attempt to save a car that already exists
    Given A car with number "ABC1234" already exists
    When I save the car
    Then I should receive a CarAlreadyExistsException

  Scenario: Update a car successfully
    Given A car with id 1 exists and saved
    And A car with number "ABC1234" does not exist
    When I update the car
    Then The car should be updated successfully

  Scenario: Attempt to update a car that already exists
    Given A car with id 1 exists and saved
    And A car with number "ABC1234" already exists for update
    When I update the car
    Then I should receive a CarAlreadyExistsException

  Scenario: Change restrictions status for an existing car
    Given A car with id 1 exists and saved
    When I change the restrictions status of the car with id 1
    Then The car restriction status should be changed

  Scenario: Attempt to change restrictions status for a non-existing car
    Given A car exists with id 99 does not exist
    When I change the restrictions status of the car with id 99
    Then I should receive a CarNotExistsException with id 99