@govtodo
Feature: Txn State with client correlation API test (Transfers)


  Scenario: Get Txn based on request type as transfers Test with Auth
    Given I create a new clientCorrelationId
    And I have tenant as "gorilla"
    And I can mock TransactionChannelRequestDTO
    When I call inbound transfer api with client correlation id expected status 200
    Given I have same clientCorrelationId
    And I have request type as "transfers"
    When I call the txn State with client correlation id expected status of 200
    Then I should get non empty response

