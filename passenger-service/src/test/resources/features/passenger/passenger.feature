Feature: Passenger
  Scenario: Get passenger by id
    Given A passenger with id 1 exists
    When I request the passenger with id 1 from getById method
    Then I should receive the passenger details with id 1

  Scenario: Get passenger by id that not exist
    Given A passenger exists with id 99 does not exist
    When I request the passenger with id 99 from getById method
    Then I should receive a PassengerNotExistsException with id 99

  Scenario: Save a new passenger successfully
    Given A passenger with email "test@example.com" and phone number "+375331234567" does not exist
    When I save the passenger
    Then The passenger should be saved successfully

  Scenario: Attempt to save a passenger that already exists
    Given A passenger with email "test@example.com" and phone number "+375331234567" already exists
    When I save the passenger
    Then I should receive a PassengerAlreadyExistsException

  Scenario: Update a passenger successfully
    Given A passenger with id 1 exists and saved
    And A passenger with email "test@example.com" and phone number "+375331234567" does not exist
    When I update the passenger with id 1
    Then The passenger should be updated successfully

  Scenario: Attempt to update a passenger that already exists
    Given A passenger with id 1 exists and saved
    And A passenger with email "test@example.com" and phone number "+375331234567" already exists for update
    When I update the passenger with id 1
    Then I should receive a PassengerAlreadyExistsException

  Scenario: Change restrictions status for an existing passenger
    Given A passenger with id 1 exists and saved
    When I change the restrictions status of the passenger with id 1
    Then The passenger restriction status should be changed

  Scenario: Attempt to change restrictions status for a non-existing passenger
    Given A passenger exists with id 99 does not exist
    When I change the restrictions status of the passenger with id 99
    Then I should receive a PassengerNotExistsException with id 99