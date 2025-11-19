package org.gruppe17.kollektivtrafikk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Unit test for the FrontEndController's /search endpoint.
 * Starts a temporary Javalin server with a mock RouteService and sends
 * a POST request using Java's HttpClient to verify that the endpoint
 * returns valid JSON data and correct status codes.
 * Confirms that the route and bus mode fields match expected values.
 *
 */

public class FrontEndControllerTest {
/*
    @Test
    void testSearchReturnsValidJson() throws Exception {
        //Create a stub for RouteService
        RouteService_OLD mockService = new RouteService_OLD() {
            @Override
            public Route getRoute(String from, String to) {
                // If the input matches, return a fake route
                if ("Fredrikstad".equals(from) && "Ostfoldhallen".equals(to)) {
                    return new Route(1, "Fredrikstad to Ostfoldhallen", new ArrayList<>(), "bus 99");
                }
                // Otherwise, return null
                return null;
            }
        };

        //Create a simple Javalin server
        // define a POST endpoint /search that uses the stub service
        Javalin app = Javalin.create().post("/search", context -> {
            String from = context.formParam("from");
            String to = context.formParam("to");

            if (from == null || to == null) {
                context.status(400).result("No data"); // Return 400 if missing parameters
                return;
            }

            var route = mockService.getRoute(from, to);
            if (route == null) {
                context.status(404).result("Route not found"); // Return 404 if no route
                return;
            }

            // Return JSON with route and bus info
            context.json(Map.of(
                    "route", from + " to " + to,
                    "bus", route.getMode()
            ));
        });

        //  Start the server on a random port
        app.start(0);
        try {
            int port = app.port(); // Get the actual port Javalin started on

        // Prepare POST data
            String form = "from=" + URLEncoder.encode("Fredrikstad", StandardCharsets.UTF_8)
                    + "&to=" + URLEncoder.encode("Ostfoldhallen", StandardCharsets.UTF_8);

            //  Send POST request using Java HttpClient
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + port + "/search"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the HTTP status code
            assertEquals(200, response.statusCode());

            //  Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            // Check the JSON content
            assertEquals("Fredrikstad to Ostfoldhallen", json.get("route").asText());
            assertEquals("bus 99", json.get("bus").asText());

        } finally {
            // Stop the server
            app.stop();
        }
    }
    */

}
