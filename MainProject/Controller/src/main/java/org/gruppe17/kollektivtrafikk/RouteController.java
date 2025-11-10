package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.service.RouteService;

import java.util.List;

public class RouteController {

    public static void register(Javalin app) {

        // GET all routes
        app.get("/api/routes", context -> {
            context.json(RouteService.getAllRoutes());
        });

        // POST: add route
        app.post("/api/routes", context -> {
            String name = context.formParam("name");

            if (name == null || name.isBlank()) {
                context.status(400).result("Route name is required");
                return;
            }

            Route newRoute = new Route(0, name);
            RouteService.addRoute(newRoute);
            context.status(201).result("Route added");
        });

        // DELETE route by ID
        app.delete("/api/routes/{id}", context -> {
            int id = Integer.parseInt(context.pathParam("id"));
            List<Route> routes = RouteService.getAllRoutes();

            Route toDelete = routes.stream()
                    .filter(r -> r.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (toDelete == null) {
                context.status(404).result("Route not found");
                return;
            }

            RouteService.deleteRoute(toDelete);
            context.status(200).result("Route deleted");
        });
    }
}
