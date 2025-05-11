// src/main/java/com/tikkie/person/PersonStack.java
package com.tikkie.person;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Code;
import software.constructs.Construct;
import java.util.List;

public class PersonStack extends Stack {
    public PersonStack(final Construct scope, final String id) {
        this(scope, id + environment, null);
    }

    public PersonStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // DynamoDB Table
        Table personTable = Table.Builder.create(this, "PersonsTable")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
            .tableName("Persons")
            .removalPolicy(software.amazon.awscdk.RemovalPolicy.DESTROY)
            .build();

        // Create Person Lambda
        Function createPersonFunction = Function.Builder.create(this, "CreatePersonFunction")
            .runtime(Runtime.JAVA_17)
            .handler("com.tikkie.person.handler.CreatePersonHandler::handleRequest")
            .code(Code.fromAsset("target/person-service-1.0-SNAPSHOT.jar"))
            .environment(java.util.Map.of("TABLE_NAME", personTable.getTableName()))
            .timeout(Duration.seconds(20))
            .build();

        createPersonFunction.addToRolePolicy(PolicyStatement.Builder.create()
            .effect(Effect.ALLOW)
            .actions(List.of("events:PutEvents"))
            .resources(List.of("*"))
            .build());

        // Get Persons Lambda
        Function getPersonsFunction = Function.Builder.create(this, "GetPersonsFunction")
            .runtime(Runtime.JAVA_17)
            .handler("com.tikkie.person.handler.GetPersonsHandler::handleRequest")
            .code(Code.fromAsset("target/person-service-1.0-SNAPSHOT.jar"))
            .environment(java.util.Map.of("TABLE_NAME", personTable.getTableName()))
            .timeout(Duration.seconds(10))
            .build();

        // Health Check Lambda
        Function healthCheckFunction = Function.Builder.create(this, "HealthCheckFunction")
            .runtime(Runtime.JAVA_17)
            .handler("com.tikkie.person.handler.HealthCheckHandler::handleRequest")
            .code(Code.fromAsset("target/person-service-1.0-SNAPSHOT.jar"))
            .build();

        // Grant permissions to Lambdas
        personTable.grantReadWriteData(createPersonFunction);
        personTable.grantReadData(getPersonsFunction);

        // API Gateway
        LambdaRestApi api = LambdaRestApi.Builder.create(this, "PersonServiceApi")
            .handler(healthCheckFunction) // default handler
            .proxy(false)
            .build();

        Resource personResource = api.getRoot().addResource("person");
        personResource.addMethod("POST", new software.amazon.awscdk.services.apigateway.LambdaIntegration(createPersonFunction));
        personResource.addMethod("GET", new software.amazon.awscdk.services.apigateway.LambdaIntegration(getPersonsFunction));

        api.getRoot().addResource("health").addMethod("GET", new software.amazon.awscdk.services.apigateway.LambdaIntegration(healthCheckFunction));
    }

    public static void main(final String[] args) {
        App app = new App();
        new PersonStack(app, "PersonServiceStack");
        app.synth();
    }
}
