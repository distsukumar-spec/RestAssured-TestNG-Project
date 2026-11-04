Feature: E2E Customer Life Cycle

  Scenario: Customer Life Cycle - Create, Retrieve, Update, Create Account and Delete Customer Profile

    Given Create New Customer
      | firstName | lastName |
      | Justin    | Hurwitz |
    When Retrieve the created customer
    Then Update the customer contact details
    |email                           |   work          |     home  |
    | Justin.Hurwitz@BreakingBad.com | (010) - 7324521 | (123) - 1234567 |
    And Verify the contact details updated
    Then Create below Accounts for the Customer
    | account          | accountType | initialBalance |
    | savingsAccount   | Savings    | 1000.00         |
    | currentAccount   | Business   | 200000.00       |
    And Verify the Accounts created successfully
    Then Delete the created Customer
    And Verify the Customer Profile is unable to retrieve
    And Verify the respective errorMsg received on retrieving a deleted customer
    | errorMsg |
    | 404 - Customer data not found |
    And Verify the Customer Profile is deleted from Customer List
      | firstName | lastName |
      | Justin    | Hurwitz |
    And Verify the Customer associated accounts are deleted
      | account          |
      | savingsAccount   |
      | currentAccount |

  Scenario Outline: Test Successful Fund Transfer

    Given Create New Customer
      | firstName | lastName |
      | WalterWhite    | Heisenberg |
    And Create new Account "<srcAccount>" with <srcAccountBalance>
    And Create new Account "<destAccount>" with <destAccountBalance>
    Then Retrieve the account "<srcAccount>" balance <transferAmount> before transfer
    And Retrieve the total no.of Transactions from Transaction History for the account "<srcAccount>" before transfer
    And Retrieve the account "<destAccount>" balance <transferAmount> before transfer
    And Retrieve the total no.of Transactions from Transaction History for the account "<destAccount>" before transfer
    When Transfer <transferAmount> from Account "<srcAccount>" to Account "<destAccount>"
    Then Verify a "Debit" Transaction entry in Account "<srcAccount>" transaction history
    Then Verify the "Debit" changes of transferred amount <transferAmount> in Account "<srcAccount>"
    And Verify the decreased balance in the account "<srcAccount>" after transfer <transferAmount>
    Then Verify a "Credit" Transaction entry in Account "<destAccount>" transaction history
    And Verify the increased balance in the account "<destAccount>" after transfer <transferAmount>
    And Verify the "Credit" changes of transferred amount <transferAmount> in Account "<destAccount>"

    Examples:
      | srcAccount | srcAccountBalance  | destAccount | destAccountBalance |   transferAmount  |
      | src        |5000.00             | dest       |10000.00             |  2999.99          |


    Scenario Outline: Test Rejection for invalid fund transfers
    Given Create New Customer
      | firstName | lastName |
      | Esha    | Tamang |
    And Create new Account "<srcAccount>" with <ExpectedSrcBalance>
    And Create new Account "<destAccount>" with <ExpectedDstBalance>

    Then Retrieve the account "<srcAccount>" balance <ExpectedSrcBalance> before transfer
    And Retrieve the total no.of Transactions from Transaction History for the account "<srcAccount>" before transfer

    And Retrieve the account "<destAccount>" balance <ExpectedDstBalance> before transfer
    And Retrieve the total no.of Transactions from Transaction History for the account "<destAccount>" before transfer

    When Transfer <invalidAmount> from Account "<srcAccount>" to Account "<destAccount>"
    Then Verify Expected API Failure with Status Code <StatusCode>
    Then Verify Expected API Failure with Error Message "<ErrorMsg>"

    Then Verify No new "Debit" Transaction entry in Account "<srcAccount>" transaction history
    And Verify the balance remains unchanged in the account "<srcAccount>" for an invalid transfer

    Then Verify No new "Credit" Transaction entry in Account "<destAccount>" transaction history
    And Verify the balance remains unchanged in the account "<destAccount>" for an invalid transfer


    Examples:
    |srcAccount | destAccount  | invalidAmount | StatusCode | ErrorMsg       | ExpectedSrcBalance    | ExpectedDstBalance |
    |src       |dest      | 0.00      | 400        | Amount should be greater than 0 | 1000.00             | 2000.00          |
    |src       |dest      | 3000.00   | 400        | Insufficient Funds | 1000.00                   | 2000.00          |
    |src       |dest      | -1.00      | 400        | Insufficient Funds | 1000.00             | 2000.00          |

      Scenario: Test Duplicate Customer Creation
        Given Create New Customer
          | firstName | lastName |
          | Justin    | Hurwitz |
        When attempting to create a same customer instance
        Then Verify Expected API Failure with Status Code 400
        Then Verify Expected API Failure with Duplicate Customer Error Message

        Scenario: Test Duplicate Account Creation
          Given Create New Customer
          | firstName | lastName |
          | Justin    | Hurwitz |
          Then Create below Accounts for the Customer
          | account          | accountType | initialBalance |
          | savingsAccount   | Savings    | 1000.00         |
          When attempting to create a duplicate account
          Then Verify Expected API Failure with Status Code 400
          Then Verify Expected API Failure with Duplicate Account Error Message

























