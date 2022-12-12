Feature: User-BusinessLogic communication

  Scenario: I create booking and then chat is created
    When I create user with role Client
    And I create user with role Lessor
    Then Client is created on BusinessLogic
    And Lessor is created on BusinessLogic