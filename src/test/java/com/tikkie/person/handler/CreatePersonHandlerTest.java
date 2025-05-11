package com.tikkie.person.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreatePersonHandlerTest {

    private CreatePersonHandler handler;
    private Context context;

    @BeforeEach
    public void setup() {
        handler = new CreatePersonHandler();
        context = new TestContext(); // You can implement a basic TestContext if needed
    }

    @Test
    public void testValidCreatePersonRequest() {
        String validInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "phoneNumber": "+1234567890",
          "address": {
            "street": "Keizersgracht 123",
            "city": "Amsterdam",
            "postalCode": "1015CJ"
          }
        }
        """;

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent()
                .withBody(validInput);

        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Person created successfully"));
    }

    @Test
    public void testInvalidCreatePersonRequest_MissingFirstName() {
        String invalidInput = """
        {
          "lastName": "Doe",
          "phoneNumber": "+1234567890",
          "address": {
            "street": "Keizersgracht 123",
            "city": "Amsterdam",
            "postalCode": "1015CJ"
          }
        }
        """;

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent()
                .withBody(invalidInput);

        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("Validation failed"));
    }
}
