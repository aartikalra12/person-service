# Person Service – Serverless Microservice on AWS

This is a serverless Java-based Person microservice deployed via AWS CDK. It supports creating and retrieving persons using API Gateway, Lambda, and DynamoDB.

---

## 📆 Tech Stack
- **Java 17**
- **AWS CDK (v2)**
- **API Gateway**
- **AWS Lambda**
- **Amazon DynamoDB**
- **Maven**

---

## 🛠️ Setup

### 1. Clone and Build the Project
```bash
git clone https://github.com/aartikalra12/person-service.git
cd person-service
mvn clean package
```

### 2. Install AWS CLI
Ensure the AWS CLI is installed:
```bash
aws --version
```

### 3. Configure AWS Credentials
Run this command to configure your AWS credentials (needed before deployment):
```bash
aws configure
```
You’ll be prompted for:
- AWS Access Key ID
- AWS Secret Access Key
- Default region (e.g., eu-west-1)
- Output format (e.g., json)

This stores credentials in `~/.aws/credentials` and is required before using any AWS CDK or CLI commands.

### 4. Verify Credentials
```bash
aws sts get-caller-identity
```
If credentials are valid, you’ll see IAM user details.

### 5. Create cdk.json
Ensure you have a `cdk.json` file in the root of your project with the following content (context can change based on the type of environment - dev/prod):
```json
{
  "app": "mvn exec:java -Dexec.mainClass=com.tikkie.person.PersonStack",
  "context": {
    "environment": "dev"
  }
}
```

### 6. Bootstrap CDK (if running CDK for the first time)
Use the following command with your AWS account ID and region:
```bash
cdk bootstrap aws://123456789012/eu-west-1
```
Replace with your actual AWS account ID and region.

### 7. Synthesize and Deploy the CDK Stack
Ensure the JAR is compiled and present:
```bash
mvn compile exec:java -Dexec.mainClass=com.tikkie.person.PersonStack
```
Then synthesize and deploy:
```bash
cdk synth
cdk deploy
```

---

## 📬 API Endpoints

### POST /person
Create a new person
```json
{
  "firstName": "Aarti",
  "lastName": "Kalra",
  "phone": "+1234567890",
  "address": {
      "street": "Amsterdam 1",
      "postalCode": "1234AB",
      "city": "Amsterdam",
      "country": "Netherlands"
  }
}
```

### GET /person
Fetch all persons

### GET /health
Health check

---

## 🔧 Example `curl` Commands

### 🔍 Get API ID from Deployed Stack (CLI)
```bash
aws cloudformation describe-stacks --stack-name PersonServiceStack \
  --query "Stacks[0].Outputs"
```
This command returns the stack outputs including the API Gateway URL. Extract the API ID from that URL.


### 🆕 Create a Person
```bash
curl -X POST https://<api-id>.execute-api.<region>.amazonaws.com/prod/person \
  -H "Content-Type: application/json" \
  -d '{
     "firstName": "D",
     "lastName": "Mandal",
     "phone": "+31123456789",
     "address": {
        "street": "Amsterdam 1",
        "postalCode": "1234AB",
        "city": "Amsterdam",
        "country": "Netherlands"
     }
  }'
```

### 📅 Get All Persons
```bash
curl https://<api-id>.execute-api.<region>.amazonaws.com/prod/person
```

### ❤️ Health Check
```bash
curl https://<api-id>.execute-api.<region>.amazonaws.com/prod/health
```

---

## 🚑 Troubleshooting

### ❌ `InvalidClientTokenId` or `security token is invalid`
**Fix:** Run `aws configure` again to reconfigure your credentials.

### ❌ `--app is required either in command-line, in cdk.json or in ~/.cdk.json`
**Fix:** Ensure your `cdk.json` file contains the correct app config, or pass `--app` flag explicitly:
```bash
cdk synth --app "mvn exec:java -Dexec.mainClass=com.tikkie.person.PersonStack"
```

### ❌ `Specify an environment name like 'aws://123456789012/eu-west-1'...`
**Fix:** Use full bootstrap command:
```bash
cdk bootstrap aws://123456789012/eu-west-1
```
Replace with your account ID and region.

### ❌ `package software.amazon.awscdk does not exist`
**Fix:** Check that dependencies are correctly added in your `pom.xml`. Use the correct version like `2.139.0`.

### ❌ `Cannot find asset` during `cdk synth`
**Fix:** Ensure the `person-service-1.0-SNAPSHOT.jar` is built and located in the `target/` directory.

### ❌ Lambda `timeout`
**Fix:** The handler might be stuck or unable to reach DynamoDB. Increase memory or check the code logic.

---

## 📁 Folder Structure
```
person-service/
├── src/
│   └── main/java/com/tikkie/person/
│       ├── PersonStack.java
│       ├── handler/
│       │   ├── CreatePersonHandler.java
│       │   ├── GetPersonsHandler.java
│       │   └── HealthCheckHandler.java
│       └── model/Person.java
├── target/person-service-1.0-SNAPSHOT.jar
├── pom.xml
├── cdk.json
```

---
