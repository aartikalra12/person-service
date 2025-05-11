# 🚀 Tikkie - Person Service (Serverless)

This project is a **serverless Java-based microservice** to manage persons. It is designed using **AWS Lambda, API Gateway, DynamoDB**, and **AWS CDK (Java)**. It exposes a `/person` REST endpoint to create and retrieve persons and a `/health` endpoint for health checks.

---

## 📁 Project Structure

```
src/
└── main/java/com/tikkie/person/
    ├── handler/                   # Lambda handlers
    ├── model/                     # Person model
    └── PersonStack.java           # CDK infrastructure definition
```

---

## 🛠️ Prerequisites

- Java 17+
- Maven
- AWS CLI configured with valid credentials
- Node.js + NPM
- AWS CDK CLI v2 (install: `npm install -g aws-cdk`)
- GitHub repo (optional, for CI/CD integration)

---

## ✅ Setup Instructions

### 1. Clone and build the Lambda JAR

```bash
git clone <your-repo-url>
cd person-service
mvn clean package
```

Make sure the JAR is generated at:
```
target/person-service-1.0-SNAPSHOT.jar
```

### 2. Bootstrap CDK (once per AWS account/region)

```bash
cdk bootstrap
```

### 3. Deploy the Stack

```bash
cdk deploy --app "mvn exec:java -Dexec.mainClass=com.tikkie.person.PersonStack"
```

This deploys:
- 3 Lambda functions (Create, Get, Health)
- 1 API Gateway
- 1 DynamoDB table (`Persons`)

---

## 🌐 API Endpoints

| Method | Endpoint            | Description          |
|--------|---------------------|----------------------|
| GET    | `/health`           | Health check         |
| POST   | `/person`           | Create new person    |
| GET    | `/person`           | List all persons     |

---

## 📦 Environment Variables

Each Lambda uses:

```bash
TABLE_NAME=Persons
```

---

## 🧪 Testing

### Health check

```bash
curl https://<your-api-id>.execute-api.<region>.amazonaws.com/prod/health
```

### Create a person

```bash
curl -X POST https://<your-api-id>.execute-api.<region>.amazonaws.com/prod/person \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John", "lastName":"Doe", "phone":"1234567890", "address":"123 Main St"}'
```

### Get all persons

```bash
curl https://<your-api-id>.execute-api.<region>.amazonaws.com/prod/person
```

---

## 🧰 Troubleshooting

### ❌ `Missing download info for actions/upload-artifact@v3`
- Caused by a misconfigured GitHub Action.
- Fix: Ensure you're referencing a valid version in `.github/workflows/*.yml`.

### ❌ `The security token included in the request is invalid`
- Fix: Run `aws configure` again to set proper credentials.

### ❌ `--app is required either in command-line, in cdk.json or in ~/.cdk.json`
- Fix: Use the full command:
  ```bash
  cdk deploy --app "mvn exec:java -Dexec.mainClass=com.tikkie.person.PersonStack"
  ```

### ❌ `ClassNotFoundException: com.tikkie.person.PersonStack`
- Fix: Ensure the fully qualified class name matches the file path.
- Ensure you're compiling with Maven:
  ```bash
  mvn clean compile
  ```

### ❌ `package software.amazon.awscdk does not exist`
- Fix: Add the correct Maven dependencies (see below).

### ❌ `Cannot find asset at` during `cdk synth`
- Fix: Ensure JAR is built with:
  ```bash
  mvn clean package
  ```

### ❌ Lambda Timeout (Internal Server Error)
- Fix: Add timeouts to all Lambda definitions:
  ```java
  .timeout(Duration.seconds(10))
  ```
- Also, add logging in handler classes to debug cold starts or logic issues.

---

## 📦 Maven Dependencies

Make sure you include the following in your `pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>software.amazon.awscdk</groupId>
    <artifactId>aws-cdk-lib</artifactId>
    <version>2.139.0</version>
  </dependency>
  <dependency>
    <groupId>software.constructs</groupId>
    <artifactId>constructs</artifactId>
    <version>10.3.0</version>
  </dependency>
</dependencies>
```

---

## 📌 Notes

- Java cold starts are longer—keep timeouts >= 10s.
- Avoid hardcoding ARNs or sensitive values—use environment variables.
- Use DynamoDB best practices (e.g., partition key selection, scaling).


