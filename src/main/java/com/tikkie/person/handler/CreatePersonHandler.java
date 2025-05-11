package com.tikkie.person.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.tikkie.person.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CreatePersonHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String TABLE_NAME = "Persons";
    private final EventBridgeClient eventBridge = EventBridgeClient.builder().build();
    private static final Logger logger = LoggerFactory.getLogger(CreatePersonHandler.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            Person person = objectMapper.readValue(request.getBody(), Person.class);
            person.setId(UUID.randomUUID().toString());

            Table table = dynamoDB.getTable(TABLE_NAME);
            table.putItem(new Item()
                    .withPrimaryKey("id", person.getId())
                    .withString("firstName", person.getFirstName())
                    .withString("lastName", person.getLastName())
                    .withString("phone", person.getPhone())
                    .withMap("address", objectMapper.convertValue(person.getAddress(), java.util.Map.class)));

            publishPersonCreatedEvent(person);

            return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody("Person created with ID: " + person.getId());
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error: " + e.getMessage());
        }
    }

    private void publishPersonCreatedEvent(Person person) {
        PutEventsRequestEntry eventEntry = PutEventsRequestEntry.builder()
            .source("com.tikkie.person")
            .detailType("PersonCreated")
            .eventBusName("default")
            .detail("{\"firstName\":\"" + person.getFirstName() + "\","
                    + "\"lastName\":\"" + person.getLastName() + "\","
                    + "\"phoneNumber\":\"" + person.getPhone() + "\"}")
            .build();
    
        PutEventsRequest request = PutEventsRequest.builder()
            .entries(eventEntry)
            .build();
    
        PutEventsResponse response = eventBridge.putEvents(request);

        System.out.println("EventBridge response: " + response);
    }
}
