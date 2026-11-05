# LTM Assignment – API Automation Framework

A Java-based API automation project for testing a banking application's customer and account workflows. It uses REST Assured for API calls, Cucumber for BDD scenarios, JUnit Platform to run the Cucumber tests, and Maven for dependency and build management.

## Project Structure

```text
LTM-Assignment-ver1/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── pojo/
    │   │   │   ├── AccountInformation.java
    │   │   │   ├── AddressDetails.java
    │   │   │   ├── BankInformation.java
    │   │   │   ├── ContactDetails.java
    │   │   │   ├── CustomerDetails.java
    │   │   │   ├── ErrorMsg.java
    │   │   │   ├── TransactionDetails.java
    │   │   │   └── TransferDetails.java
    │   │   └── utils/
    │   │       ├── APIBuilder.java
    │   │       ├── AppSpecificAssertions.java
    │   │       ├── ConfigReader.java
    │   │       ├── EndPoints.java
    │   │       ├── HeaderConfig.java
    │   │       └── ResponseValidator.java
    │   └── resources/
    │       ├── schemas/
    │       │   ├── AccountInformation.json
    │       │   ├── AddressDetails.json
    │       │   ├── BankInformation.json
    │       │   ├── ContactDetails.json
    │       │   ├── CustomerDetails.json
    │       │   ├── ErrorMsg.json
    │       │   ├── TransactionDetails.json
    │       │   └── TransferDetails.json
    │       └── config.properties
    └── test/
        ├── java/
        │   ├── stepDefinitions/
        │   │   ├── BaseSteps.java
        │   │   ├── E2E_CustomerLifeCycle_StepDefs.java
        │   │   ├── ScenarioContext.java
        │   │   └── ScenarioHooks.java
        │   ├── testRunners/
        │   │   └── E2E_CustomerLifeCycle_TestRunner.java
        │   └── TestSuite.java
        └── resources/
            ├── features/
            │   └── E2E-CustomerLifeCycle.feature
            └── junit-platform.properties
```

## Folder and File Overview

### `src/main/java`

Contains the reusable application code used by the test framework.

- **`pojo/`** – Java model classes representing API request and response data, including customers, addresses, accounts, transactions, and transfers.
- **`utils/`** – Shared framework utilities:
  - `APIBuilder` – common methods for sending API requests.
  - `AppSpecificAssertions` – application-specific assertion helpers.
  - `ConfigReader` – reads environment and application configuration.
  - `EndPoints` – central location for API endpoint definitions.
  - `HeaderConfig` – provides request headers.
  - `ResponseValidator` – reusable response and JSON data validations.

### `src/main/resources`

- **`config.properties`** – environment configuration. The supplied configuration defines `qa` and `stage` URLs and sets `qa` as the active environment.
- **`schemas/`** – JSON schema files used to validate API response structures.

### `src/test/java`

Contains the automated test implementation and execution setup.

- **`stepDefinitions/`** – Cucumber step implementations and shared test support:
  - `BaseSteps` – common step-level functionality.
  - `E2E_CustomerLifeCycle_StepDefs` – implementation of the customer lifecycle and fund-transfer steps.
  - `ScenarioContext` – shared data/context for a test scenario.
  - `ScenarioHooks` – Cucumber scenario setup and teardown hooks.
- **`testRunners/`** – JUnit Platform runner that discovers and executes Cucumber feature files.
- **`TestSuite.java`** – test suite entry point/support class.

### `src/test/resources`

- **`features/E2E-CustomerLifeCycle.feature`** – business-readable scenarios for customer lifecycle operations and successful/invalid fund transfers.
- **`junit-platform.properties`** – JUnit Platform configuration.

### `pom.xml`

Maven project configuration. It defines the Java 17 compiler settings, framework dependencies (including REST Assured, Cucumber, JUnit Platform, and TestNG), and the Maven Surefire test runner configuration.

## Scenarios Covered

The feature file currently describes these API workflows:

1. Create a customer and retrieve the customer profile.
2. Update and verify customer contact details.
3. Create an account and verify it.
4. Delete the customer and verify that the customer and associated accounts are deleted.
5. Transfer funds between two accounts and verify debit/credit transactions and balances.
6. Validate rejected transfers, including zero-amount and insufficient-funds cases.

## Prerequisites

- Java Development Kit (JDK) 17
- Apache Maven
- Network access to the configured API environment

## Configuration

Update `src/main/resources/config.properties` to point to the required environment:

```properties
active=https://orca-app-8ytxl.ondigitalocean.app/bank-api
alternate=https://seahorse-app-tam6f.ondigitalocean.app/bank-api

# Active environment
env=active
```

Change `env` to the environment key you want to run against (`active` or `alternate`).

## Run the Tests

From the project root directory, run:

```bash
mvn clean test  
```
Run with selective env & override configured env in case of default configured env server is down

```bash
mvn clean test -Denv=active 
```

Maven Surefire is configured to execute `E2E_CustomerLifeCycle_TestRunner`.

## Technology Stack

- Java 17
- Maven
- REST Assured
- Cucumber (Gherkin)
- 
- JUnit Platform
- TestNG assertions
- JSON Schema validation

## 1. Test Strategy

### Objective
Validate critical Banking API business workflows using a risk-based functional testing approach, focusing on customer lifecycle operations, account management, financial transaction accuracy, data persistence, and error handling.

### Testing Approach
The framework follows a Behavior-Driven Development (BDD) approach using Cucumber and Gherkin, with Java, RestAssured, JUnit Platform, and Maven. Scenarios are prioritized based on business impact, particularly financial transaction integrity, customer data consistency, and invalid operation handling.

### Implemented Test Coverage
1. **Customer Lifecycle:** Steps cover customer creation, retrieval, contact updates, account creation, deletion, and post-deletion verification.
2. **Successful Fund Transfer:** Steps cover transfers between two customer accounts, including balance calculations and debit/credit transaction-history validation.
3. **Rejected Fund Transfers:** Parameterized scenarios cover zero, negative, and insufficient-funds amounts, verifying rejection responses, unchanged balances, and unchanged transaction counts.
4. **Duplicate Customer Validation:** Attempt to create a customer profile using existing customer details and verify the expected HTTP `400` response and duplicate-customer error message.
5. **Duplicate Account Validation:** Attempt to create an account using existing account details and verify the expected HTTP `400` response and duplicate-account error message.

### Execution Scope

- The automation suite covers customer lifecycle operations, account creation, successful fund transfers, rejected fund transfers, and additional negative validation scenarios.
- All implemented scenarios are included in the test execution scope and can be executed through the Maven test runner.
- Actual execution outcomes, including passed, failed, and skipped scenarios, are documented separately in the execution results.

### Test Design Principles
- **BDD-based test design:** Gherkin scenarios map to Java step definitions.
- **Reusable API operations:** `APIBuilder` centralizes HTTP operations; `EndPoints` maintains endpoint definitions.
- **POJO-based data modeling:** Structured customer, account, contact, and transaction models.
- **Synthetic test data:** Fake-data factory plus DataTables and Scenario Outline inputs.
- **Data-driven testing:** DataTables and Scenario Outline examples reuse steps across cases.
- **Centralized validation:** `ResponseValidator` and `AppSpecificAssertions` support status, body, JSON-path, schema, and business assertions.
- **Financial precision:** `BigDecimal` is used for transfer amounts and expected balance calculations, subject to the response-parsing limitation below.
- **Runtime data management:** Runtime maps track account details and transaction counts across steps.
- **Environment configuration:** `ConfigReader` supports properties and Maven `-Denv` selection.
- **Failure diagnostics:** Scenario logging, status/response-time details, and response attachments.
- **Reporting:** Cucumber HTML and JSON reports are configured.

### Assumptions
1. The assessment API supports synthetic customer and account creation.
2. Explicit opening balances are supported during account creation.
3. Transfers occur between accounts belonging to the same customer.
4. Transfers use the same currency, have no fee, and do not permit overdrafts.
5. Successful transfers return HTTP `200`; non-positive and insufficient-funds transfers return HTTP `400`.
6. Rejected transfers leave balances unchanged and create no successful debit/credit entries.
7. Customer deletion removes associated accounts.
8. Duplicate-account creation is expected to return HTTP `400` in the implemented test.

## 2. Key Risks and Mitigation

| Priority | Key risk                                                        | Framework validation or mitigation |
|---|-----------------------------------------------------------------|---|
| P0 — Critical | Incorrect fund transfer balances                                | `BigDecimal` calculations and account retrieval before/after transfer |
| P0 — Critical | Rejected transfers change balances                              | Compare both account balances before and after rejection |
| P0 — Critical | Transaction history differs from fund movements                 | Runtime transaction-count tracking plus debit/credit detail validation |
| P1 — High | Customer updates are not persisted                              | Retrieve and compare customer data after update |
| P1 — High | Customer deletion leaves linked accounts accessible             | Verify customer and linked accounts are not retrievable after deletion |
| P1 — High | Unexpected responses are treated as acceptable during execution | Tighten status assertions, especially dependency-related checks |
| P1 — High | Test-created records persist after failures                     | Add API-level cleanup to the scenario `@After` hook |
| P2 — Medium | Shared runtime state affects independent execution              | Move mutable data to scenario-scoped storage |
| P2 — Medium | API outages or defects block execution                          | Record diagnostics and report blocked scenarios separately |

## 3. Known Omissions and Limitations
1.  Unexpected HTTP 302 responses require investigation. Account retrieval returned redirects where the test expected HTTP 200. Verify the API contract, Location header, and client redirect handling before classifying this as a confirmed API defect.
2.  Scenario isolation is limited. Shared static runtime maps and response fields can introduce cross-scenario dependencies, especially if parallel execution is introduced.
3.  Additional negative cases are not covered. Examples include unknown customer/account references and malformed request payloads. These are optional extensions beyond the selected additional negative scenarios.

###  Next Priorities
1. Investigate HTTP 302 account retrieval responses and rerun the affected scenarios after confirming expected behavior.
2. Implement API-level cleanup in Cucumber hooks with failure-safe behavior and appropriate cleanup ordering.
3. Introduce scenario-scoped test context to avoid reliance on mutable static state.
4. Add further negative coverage if time remains after required acceptance criteria are consistently passing.

## Actual Time Spent

**Total Effort: 28 Hours**

| Activity | Initial Implementation | After First-Round Feedback |      Total |
|---|---:|---------------------------:|-----------:|
| Requirement analysis and test strategy | 2 hrs |                      1 hrs |      3 hrs |
| Framework setup and configuration | 3 hrs |                    0.5 hrs |    3.5 hrs |
| API request builders, POJOs, and utilities | 4 hrs |                       1 hr |      5 hrs |
| Cucumber feature files and step definitions | 5 hrs |                      2 hrs |      7 hrs |
| Response validation and assertions | 2 hrs |                     1.5 hr |    3.5 hrs |
| Test execution, debugging, and defect analysis | 3 hrs |                       1 hr |      4 hrs |
| Documentation and README preparation | 1 hr |                      1 hrs |      2 hrs |
| **Total** | **20 hrs** |                  **8 hrs** | **28 hrs** |

### Effort Breakdown

**Initial Implementation — 20 Hours**

Focused on requirement analysis, framework design, API automation development, Cucumber scenario implementation, response validations, and initial test execution.

**Post First-Round Feedback — 8 Hours**

Focused on refining the existing implementation, improving validations, addressing feedback, re-executing scenarios, investigating failures, and updating documentation.

**Total Actual Effort: 28 Hours**

*The total reflects approximately 28 hours of hands-on work across initial development and post-feedback improvements. The activity-level split is an estimated allocation of the reported total.*****

## Test Execution Summary

**Evidence:** Earlier Cucumber JSON execution (`cucumber.json`), cross-referenced with `LTM Asserssment - Test Execution.xlsx`.

| Metric | Result |
|---|---:|
| Total executed Cucumber scenarios | 7 |
| Passed | 4 |
| Failed | 3 |
| Pass rate | 57.1% |

### Scenario Results

| Scenario | Result | Notes |
|---|---|---|
| Customer lifecycle — create, retrieve, update, create accounts and delete | **FAIL** | Customer GET after deletion returned HTTP 200 instead of expected 404; subsequent deletion checks skipped (BUG-003). |
| Successful fund transfer (2999.99) | **PASS** | Successful transfer, balance and debit/credit history checks passed in this execution. |
| Rejected transfer — amount 0.00 | **FAIL** | Returned HTTP 200 instead of expected 400 (BUG-004). Subsequent balance/history assertions skipped. |
| Rejected transfer — insufficient funds (3000.00) | **PASS** | Rejection and unchanged balance/history validations passed. |
| Rejected transfer — amount -1.00 | **FAIL** | Returned HTTP 200 instead of expected 400 (BUG-005). Subsequent balance/history assertions skipped. |
| Duplicate customer creation | **PASS** | Duplicate customer request rejected as expected. |
| Duplicate account creation | **PASS** | Duplicate account request rejected as expected. |

**Scope note:** Additional cases listed in the test case tracker (such as missing fields, invalid payloads, unknown identifiers, and account opening-balance boundaries) are **not executed** unless supported by a corresponding Cucumber scenario. They are not included in the seven-scenario total. A separate account retrieval status observation in the tracker does not change the successful-transfer scenario's recorded result.

## Bug Summary

| Bug ID | Defect | Priority | Severity | Evidence / current interpretation |
|---|---|---|---|---|
| BUG-001 | Account retrieval returns HTTP 302 instead of documented HTTP 200 | P1 — High | Major | Separately observed; **not counted as a failed scenario** in the earlier JSON execution. |
| BUG-002 | Retrieved account has `accountCreated: null` | P1 — High | Major | Separately reported data-quality concern; timestamp is typed as `date-time` in OpenAPI but is not explicitly required/non-null. Requires investigation. |
| BUG-003 | Deleted customer remains retrievable | P1 — High | Major | **Confirmed failed assertion in earlier JSON:** GET after DELETE returned HTTP 200 rather than expected 404. |
| BUG-004 | Zero-amount transfer is accepted | P0 — Critical | Critical | **Confirmed failed assertion in earlier JSON:** transfer of `0.00` returned HTTP 200 instead of 400. |
| BUG-005 | Negative-amount transfer is accepted | P0 — Critical | Critical | **Confirmed failed assertion in earlier JSON:** transfer of `-1.00` returned HTTP 200 instead of 400. |