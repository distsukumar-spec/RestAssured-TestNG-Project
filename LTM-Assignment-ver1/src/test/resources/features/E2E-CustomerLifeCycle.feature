Feature: E2E Customer Life Cycle

  Scenario: Customer Life Cycle - Create, Retrieve, Update, Create Account and Delete Customer Profile

    Given Create New Customer
    When Retrieve the created customer
    Then Update the customer contact details
    And Verify the contact details updated
    Then Create an Account for the Customer
    And Verify the Account created successfully
    Then Delete the created Customer
    And Verify the Customer data is deleted
    And Verify the Customer associated accounts are deleted

  Scenario Outline: Test Successful Fund Transfer

    Given Create New Customer
    And Create new Account "<srcAccount>" with 5000.00
    Then Retrieve the created Account "<srcAccount>" and Verify Account Balance 5000.00
    And Create new Account "<destAccount>" with 10000.00
    Then Retrieve the created Account "<destAccount>" and Verify Account Balance 10000.00
    When Transfer <transferAmount> from Account "<srcAccount>" to Account "<destAccount>"
    Then Verify the "Debit" Transaction of <transferAmount> in Account "<srcAccount>"
    And Verify the "Credit" Transaction of <transferAmount> in Account "<destAccount>"
    Then Retrieve the created Account "<srcAccount>" and Verify Account Balance 2000.01
    Then Retrieve the created Account "<destAccount>" and Verify Account Balance 12999.99

    Examples:
      | srcAccount | destAccount |   transferAmount |
      | src        | dest        |  2999.99         |


    Scenario Outline: Test rejection for invalid fund transfers
    Given Create New Customer
    And Create new Account "<srcAccount>" with 1000.00
    And Create new Account "<destAccount>" with 2000.00
    When Transfer <Amount> from Account "<srcAccount>" to Account "<destAccount>"
    Then Verify Expected API Failure with Status Code <StatusCode> and Error Message "<ErrorMsg>"
    And Retrieve the created Account "<srcAccount>" and Verify Account Balance <ExpectedSrcBalance>
    And Retrieve the created Account "<destAccount>" and Verify Account Balance <ExpectedDstBalance>

    Examples:
    |srcAccount | destAccount  | Amount | StatusCode | ErrorMsg       | ExpectedSrcBalance    | ExpectedDstBalance |
    |src       |dest      | 0      | 400             | errorMsg           | 1000.00             | 2000.00          |
     |src       |dest      | 3000   | 400        | Insufficient Funds | 1000.00             | 2000.00          |























