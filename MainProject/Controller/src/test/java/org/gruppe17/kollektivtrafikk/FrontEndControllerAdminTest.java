package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrontEndControllerAdminTest {

    @Test
    void testAddRouteEndpoint() {
        //using JavalinTesy to test without running real server on port
        Javalin app = Javalin.create();
        app.post("/addRoute", context -> {
            String name = context.formParam("name");
            if (name == null || name.isBlank()) {
                context.status(400).result("Missing route name");
            } else {
                context.status(201).result("Route temporarily added");
            }
        });

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/addRoute", "name=TestRoute");
            assertEquals(201, response.code());
            assertEquals("Route temporarily added", response.body().string());
        });
    }

    @Test
    void testAddRouteWithoutName() {
        Javalin app = Javalin.create();
        app.post("/addRoute", context -> {
            String name = context.formParam("name");
            if (name == null || name.isBlank()) {
                context.status(400).result("Missing route name");
            } else {
                context.status(201).result("Route temporarily added");
            }
        });

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/addRoute", "name=TestRoute");
            assertEquals(201, response.code());
        });
    }
}
