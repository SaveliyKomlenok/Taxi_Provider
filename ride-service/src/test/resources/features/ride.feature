Feature: Ride
  Scenario: Get ride by id successfully
    Given A ride with id 2 exists
    When I request the ride by id 2
    Then I should receive the ride details with id 2

  Scenario: Get ride by id that does not exist
    Given A ride with id 99 does not exist
    When I request the ride by id 99
    Then I should receive a RideNotExistsException with id 99

  Scenario: Create a ride successfully
    Given A passenger with id 2
    When I create a new ride
    Then I should receive the created ride details

  Scenario: Create a ride with a restricted passenger
    Given A restricted passenger with id 2
    When I create a new ride
    Then I should receive a PassengerRestrictedException with id 2

  Scenario: Accept a ride successfully
    Given A ride is in created status
    When I accept the ride
    Then I should receive the accepted ride details

  Scenario: Accept a ride that is not in created status
    Given A ride is in accepted status
    When I accept the ride
    Then I should receive a RideAcceptException with id 1

  Scenario: Finish a ride successfully
    Given A ride is on the way to destination
    When I finish the ride
    Then I should receive the finished ride details

  Scenario: Finish a ride that cannot be finished
    Given A ride is not found
    When I finish the ride
    Then I should receive a RideFinishException with id 1

  Scenario: Cancel a ride successfully
    Given A ride is in created status for cancel
    When I cancel the ride
    Then I should receive the canceled ride details

  Scenario: Cancel a ride that cannot be canceled
    Given A ride cannot be canceled
    When I cancel the ride
    Then I should receive a RideCancelException with id 1

  Scenario: Change ride status successfully
    Given A ride is in accepted status for change
    When I change the ride status
    Then I should receive the ride with updated status

  Scenario: Change ride status with invalid conditions
    Given A ride cannot be changed
    When I change the ride status
    Then I should receive a RideChangeStatusException with id 1