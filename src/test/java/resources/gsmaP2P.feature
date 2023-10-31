@amsintegrationtest @govtodo
Feature: GSMA Outbound Transfer test

  Scenario: GSMA Deposit-Withdrawal Transfer test
    Given I have Fineract-Platform-TenantId as "gorilla"
    When I create a set of debit and credit party
    When I call the create payer client endpoint
    Then I call the create savings product endpoint
    When I call the create savings account endpoint
    Then I call the debit interop identifier endpoint with MSISDN
    Then I approve the deposit with command "approve"
    When I activate the account with command "activate"
    Then I call the deposit account endpoint with command "deposit" for amount 100
    Given I have Fineract-Platform-TenantId as "lion"
    When I call the create payer client endpoint
    Then I call the create savings product endpoint
    When I call the create savings account endpoint
    Then I call the credit interop identifier endpoint with MSISDN
    Then I approve the deposit with command "approve"
    When I activate the account with command "activate"
    Then I call the deposit account endpoint with command "deposit" for amount 100
    Given I have tenant as "gorilla"
    Then I call the balance api for payer balance
    Given I have tenant as "gorilla"
    When I can create GSMATransferDTO with different payer and payee
    Then I call the GSMATransfer endpoint with expected status of 200
    And I should be able to parse transactionId from response
    Given I have tenant as "gorilla"
    When I call the transfer query endpoint with transactionId and expected status of 200
    Then I should have startedAt and completedAt in response
    Given I have tenant as "gorilla"
    Then I call the balance api for payer balance
    Given I have tenant as "lion"
    Then I call the balance api for payee balance



  Scenario: GSMA Deposit-Withdrawal Transfer test with bulk transfer
    Given I have Fineract-Platform-TenantId as "gorilla"
    When I create a set of debit and credit party from file "ph-ee-bulk-demo-8.csv"
    When I call the create payer client endpoint
    Then I call the create savings product endpoint
    When I call the create savings account endpoint
    Then I call the debit interop identifier endpoint with MSISDN
    Then I approve the deposit with command "approve"
    When I activate the account with command "activate"
    Then I call the deposit account endpoint with command "deposit" for amount 100
    Given I have Fineract-Platform-TenantId as "lion"
    When I call the create payer client endpoint
    Then I call the create savings product endpoint
    When I call the create savings account endpoint
    Then I call the credit interop identifier endpoint with MSISDN
    Then I approve the deposit with command "approve"
    When I activate the account with command "activate"
    Then I call the deposit account endpoint with command "deposit" for amount 100
    Given I have tenant as "gorilla"
    Then I call the balance api for payer balance
    Given I have tenant as "gorilla"
    When I have the demo csv file "ph-ee-bulk-demo-6.csv"
    And I create a new clientCorrelationId
    And I have private key
    And I generate signature
    When I call the batch transactions endpoint with expected status of 202
    And I am able to parse batch transactions response
    And I fetch batch ID from batch transaction API's response
    Then I will sleep for 2000 millisecond
    When I call the batch summary API with expected status of 200
    Then I am able to parse batch summary response
    And Status of transaction is "COMPLETED"
    And I should have matching total txn count and successful txn count in response