Feature: Driver
  Scenario: Get driver by id
    Given A driver with id 1 exists
    When I request the driver with id 1 from getById method
    Then I should receive the driver details with id 1

  Scenario: Get driver by id that not exist
    Given A driver exists with id 99 does not exist
    When I request the driver with id 99 from getById method
    Then I should receive a DriverNotExistsException with id 99

  Scenario: Save a new driver successfully
    Given A driver with email "test@example.com" and phone number "+375331234567" does not exist
    When I save the driver
    Then The driver should be saved successfully

  Scenario: Attempt to save a driver that already exists
    Given A driver with email "test@example.com" and phone number "+375331234567" already exists
    When I save the driver
    Then I should receive a DriverAlreadyExistsException

  Scenario: Update a driver successfully
    Given A driver with id 1 exists and saved
    And A driver with email "test@example.com" and phone number "+375331234567" does not exist
    When I update the driver with id 1
    Then The driver should be updated successfully

  Scenario: Attempt to update a driver that already exists
    Given A driver with id 1 exists and saved
    And A driver with email "test@example.com" and phone number "+375331234567" already exists for update
    When I update the driver with id 1
    Then I should receive a DriverAlreadyExistsException

  Scenario: Change restrictions status for an existing driver
    Given A driver with id 1 exists and saved
    When I change the restrictions status of the driver with id 1
    Then The driver restriction status should be changed

  Scenario: Attempt to change restrictions status for a non-existing driver
    Given A driver exists with id 99 does not exist
    When I change the restrictions status of the driver with id 99
    Then I should receive a DriverNotExistsException with id 99

  Scenario: Change busy status for an existing driver
    Given A driver with id 1 exists and saved
    When I change the busy status of the driver with id 1
    Then The driver busy status should be changed

  Scenario: Attempt to change busy status for a non-existing driver
    Given A driver exists with id 99 does not exist
    When I change the busy status of the driver with id 99
    Then I should receive a DriverNotExistsException with id 99