Feature: Authentication System
  Check the API for the authentication system

  Scenario: Verify successful authentication
    Given the authentication URL is "<authUrl>"
    And the content type is "<contentType>"
    And the method is "POST"
    When I authenticate with username "<username>" and password "<password>"
    Then the response status code should be 200
    And the response body contains an access token