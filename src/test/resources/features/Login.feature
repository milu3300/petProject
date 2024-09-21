Feature:
  @testLogin
  Scenario: verify login functionality
    Given base URL "https://www.saucedemo.com/"
    Then user add user name "userName"
    And user add password "password"
    And user clicks on login button
    And user successful on page

