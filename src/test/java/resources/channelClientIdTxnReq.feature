Feature: Txn State with client correlation API test (Transaction Request)

  Scenario: Get Txn based on request type as transaction request Test
    Given I create a new clientCorrelationId
    And I have tenant as "gorilla"
    When I call collection api with client correlation id expected status 200
    Given I have same clientCorrelationId
    And I have request type as "transactionsReq"
    When I call the txn State with client correlation id expected status of 200
    Then I should get non empty response