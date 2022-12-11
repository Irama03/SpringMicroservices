Feature: Booking-Chat communication

  Scenario: I create booking and then chat is created
    When I create client
    And I create lessor
    And I create room for this lessor
    And I create booking with this client and this room
    Then chat with client and lessor is created
