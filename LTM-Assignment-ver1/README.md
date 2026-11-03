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
qa=https://orca-app-8ytxl.ondigitalocean.app/bank-api
stage=https://orca-app-8ytxl.ondigitalocean.app/bank-api

# Active environment
env=qa
```

Change `env` to the environment key you want to run against (`qa` or `stage`).

## Run the Tests

From the project root directory, run:

```bash
mvn clean test  
```
Run with selective env & override configured env

```bash
mvn clean test -Denv=qa 
```

Maven Surefire is configured to execute `E2E_CustomerLifeCycle_TestRunner`.

## Technology Stack

- Java 17
- Maven
- REST Assured
- Cucumber (Gherkin)
- JUnit Platform
- TestNG assertions
- JSON Schema validation
