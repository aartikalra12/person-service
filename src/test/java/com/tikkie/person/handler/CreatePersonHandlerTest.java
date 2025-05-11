package com.tikkie.person.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikkie.person.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class CreatePersonHandlerTest {

    private CreatePersonHandler handler;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        handler = new CreatePersonHandler();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testHandleRequest_validInput_returnsSuccess() throws Exception {
        Person person = new Person("Aarti", "Kalra", "+1234567890", "Amsterdam");
        String jsonInput = objectMapper.writeValueAsString(person);

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent()
                .withBody(jsonInput);

        Context context = Mockito.mock(Context.class);

        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("Person created successfully"));
    }

    @Test
    public void testHandleRequest_invalidInput_returnsBadRequest() {
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent()
                .withBody("invalid-json");

        Context context = Mockito.mock(Context.class);

        APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid request"));
    }
} 
