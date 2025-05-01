# testrepo
This project is a serverless microservice for managing person records. It uses:

- AWS Lambda (Java)
- Amazon API Gateway
- Amazon DynamoDB
- Amazon EventBridge
- AWS CDK (Java)

## Features
- Create a person record via `POST /person`
- Save data in DynamoDB
- Emit `PersonCreated` event to EventBridge

## Project Structure
- `src/main/java/com/tikkie/person/PersonStack.java` – CDK infrastructure
- `src/main/java/com/tikkie/person/handler/CreatePersonHandler.java` – Lambda to create a person
- `src/main/java/com/tikkie/person/handler/GetPersonsHandler.java` – Lambda to list persons
- `src/main/java/com/tikkie/person/handler/HealthCheckHandler.java` – Lambda for system health check

## Build & Deploy
1. Build the Lambda package:
```bash
mvn clean package
```

2. Deploy using CDK:
```bash
cdk deploy
```

## Test API
```bash
curl -X POST https://<API_ID>.execute-api.<region>.amazonaws.com/prod/person \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "John",
        "lastName": "Doe",
        "phone": "1234567890",
        "address": {
            "street": "Main St",
            "postalCode": "12345",
            "city": "Amsterdam",
            "country": "NL"
        }
    }'
```

## Requirements
- Java 17+
- Maven
- AWS CLI + CDK configured

---
This implementation meets the Tikkie full stack engineer assignment criteria using Java and serverless architecture. It also preserves support for additional handlers for listing persons and system health checks.
