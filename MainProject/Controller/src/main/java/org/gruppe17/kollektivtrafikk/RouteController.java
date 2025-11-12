package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;

import java.util.ArrayList;
import java.util.List;



public class RouteController {

    public static void register(Javalin app, RouteService routeService, StopService stopService) {


        // GET all routes
        app.get("/api/routes", context -> {
            List <Route> routes = routeService.getAllRoutes();
            context.json(routes);
        });

        // POST: add route
        app.post("/api/routes", context -> {
            try {
                String name = context.formParam("name");
                String[] stopIds = context.formParams("stopIds").toArray(new String[0]);

                if (name == null || name.isBlank() || stopIds.length < 2) {
                    context.status(400).result("Missing required fields");
                    return;
                }

                ArrayList<Stop> stops = new ArrayList<>();
                for (String idStr : stopIds) {
                    int id = Integer.parseInt(idStr);
                    Stop stop = stopService.getStopById(id);
                    if (stop != null) stops.add(stop);
                }

                if (stops.size() < 2) {
                    context.status(400).result("At least two stops are required");
                    return;
                }

                Route newRoute = new Route(0, name, stops, null);

                boolean ok = routeService.addRoute(newRoute);
                if (ok) {
                    context.status(201).result("Route added successfully");
                } else {
                    context.status(400).result("Error adding route");
                }
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Error adding route" + e.getMessage());
            }
        });

        // DELETE route by ID
        app.delete("/api/routes/{id}", context -> {
           try {
               int id = Integer.parseInt(context.pathParam("id"));
               Route route = routeService.getRouteById(id);

               if (route == null) {
                   context.status(404).result("Route not found");
                   return;
               }

               boolean ok = routeService.deleteRoute(route);
               if (ok) {
                   context.status(200).result("Route deleted successfully");
               } else {
                   context.status(500).result("Error deleting route");
               }
           } catch (Exception e) {
               e.printStackTrace();
               context.status(400).result("Error deleting route" + e.getMessage());
           }
        });
    }
}
