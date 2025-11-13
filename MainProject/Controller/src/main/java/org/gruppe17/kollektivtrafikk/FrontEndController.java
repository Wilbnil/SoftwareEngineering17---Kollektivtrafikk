package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.service.TimetableService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.RouteService;

import java.util.List;
import java.util.Map;

/**
 * FrontEndController
 *
 * Registers user-facing endpoints (for index.html) on an existing Javalin app.
 *
 * Endpoints:
 *  GET  /api/stops  -> returns all stops (for autocomplete)
 *  POST /search     -> finds a route between two stops and returns distance + next departure
 */
public class FrontEndController {
    public static void register(Javalin app, StopService stopService, RouteService routeService, TimetableService timetableService) {

        // Redirect root ("/") to index.html (served as static file by Application config)
        app.get("/", context -> context.redirect("index.html"));

        // Return all stops
        app.get("/api/stops", context -> {
            try {
                List<Stop> stops = stopService.getAllStops();
                context.json(stops);
            } catch (Exception e) {
                e.printStackTrace();
                context.status(500).result("Error loading stops." + e.getMessage());
            }
        });

        // Search a route between two stops and compute travel data
        app.post("/search", context -> {
            try {
                String fromName = context.formParam("from");
                String toName   = context.formParam("to");
                boolean requireRoof = Boolean.parseBoolean(context.formParam("roof"));
                boolean requireAccess = Boolean.parseBoolean(context.formParam("accessible"));

                // Basic validation
                if (fromName == null || toName == null || fromName.isBlank() || toName.isBlank()) {
                    context.status(400).result("Missing input.");
                    return;
                }
                if (fromName.equalsIgnoreCase(toName)) {
                    context.status(400).result("Stops cannot be the same.");
                    return;
                }

                // Find stops
                Stop fromStop = stopService.getStopByName(fromName);
                Stop toStop = stopService.getStopByName(toName);

                if (fromStop == null || toStop == null) {
                    context.status(404).result("Stop not found.");
                    return;
                }

                // Apply filters
                if (requireRoof && (!fromStop.getRoof() || !toStop.getRoof())) {
                    context.status(400).result("Selected stops do not have roofs.");
                    return;
                }
                if (requireAccess && (!fromStop.getAccessibility() || !toStop.getAccessibility())) {
                    context.status(400).result("Selected stops are not accessible.");
                    return;
                }

                // Find possible routes between these stops
                List<Route> routes = routeService.getRouteBetweenStops(fromStop.getId(), toStop.getId());
                if (routes == null || routes.isEmpty()) {
                    context.status(404).result("No route found between these stops.");
                    return;
                }

                Route route = routes.get(0); // take the first route found

                //calculating distance
                double distance = routeService.calculateDistanceBetweenStops(fromStop, toStop);

                // get timetable for this route
                Timetable timetable = timetableService.getTimetableForRoute(route.getId());
                if (timetable == null) {
                    context.status(404).result("No timetable for this route.");
                    return;
                }


                // Respond with JSON
                context.json(Map.of(
                        "route",     fromName + " \u2192 " + toName, // that number is arrow →
                        "distance",  distance,
                        "departure", departure,
                        "arrival",   arrival,
                        "type",      route.getType()
                ));

            } catch (Exception e) {
                e.printStackTrace();
                context.status(500).result("Server error: " + e.getMessage());
            }
        });
    }
}
