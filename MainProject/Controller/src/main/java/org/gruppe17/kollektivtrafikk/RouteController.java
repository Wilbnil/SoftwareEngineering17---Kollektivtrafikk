package org.gruppe17.kollektivtrafikk;


import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.TimetableService;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * The {@code RouteController} class handles all HTTP requests related to routes.
 * It acts as communication layer between the frontend and backend services such us:
 * {@link RouteService}, {@link StopService}, and {@link TimetableService}.
 *
 * <p>This controller is used by Javalin to serve JSON respones or static HTML files
 * depending on the type of request.</p>
 *
 * Correct Usage Example:
 * <blockquote><pre>
 *     app.get("/api/routes", routeController::getAllRoutes);
 *     app.post("/admin/routes", routeController::addRoutes);
 * </pre></blockquote>
 *
 * Incorrect Usage Example:
 * <blockquote><pre>
 *     RouteController rc = new RouteController(null, null, null);
 *     rc.getAllRoutes(null);
 * </pre></blockquote>
 *
 * This class should be created once during application startup and reused.
 */
public class RouteController {

    private RouteService routeService;
    private StopService stopService;
    private TimetableService timetableService;


    /**
     * Creates a new {@code RouteController} with required service dependencies.
     * @param routeService service for handling route-related operations
     * @param stopService service for retrieving stop data
     * @param timetableService service for timetable and travel time calculations
     */
    public RouteController(RouteService routeService, StopService stopService, TimetableService timetableService) {
        this.routeService = routeService;
        this.stopService = stopService;
        this.timetableService = timetableService;
    }

    /**
     * Serves the admin HTML page from {@code /public/admin.html}
     * If the file cannot be loaded, a 500 error is returned.
     * @param context Javalin HTTP request context
     */
    public void serveAdminPage(Context context) {
        try {
            String html = new String(
                    getClass().getResourceAsStream("/public/admin.html").readAllBytes()
            );
            context.contentType("text/html").result(html);
        } catch (Exception e) {
            context.status(500).result("Error loading admin page: " + e.getMessage());
        }
    }

    /**
     * Return all available routes as JSON.
     * @param context Javalin HTTP request context
     */
    public void getAllRoutes(Context context) {
        ArrayList<Route> routes = routeService.getAllRoutes();
        context.json(routes);
    }

    /**
     * Searches for a route between two stops, calculates distance, next departure, expected arrival and travel duration.
     * <p>This method expects form parameters:</p>
     * <li>{@code from} - start stop name</li>
     * <li>{@code to} - destination stop name</li>
     * <li>{@code time} -optional,  user-selected time</li>
     * <p>Returns a JSON object containing route details.</p>
     * @param context Javalin HTTP request context
     */
    public void searchRoute(Context context) {
        try {
            String fromName = context.formParam("from");
            String toName = context.formParam("to");

            if (fromName == null || toName == null || fromName.isBlank() || toName.isBlank()) {
                context.status(400).result("Missing input.");
                return;
            }
            if (fromName.equalsIgnoreCase(toName)) {
                context.status(400).result("Stops cannot be the same.");
                return;
            }
            Stop fromStop = stopService.getStopByName(fromName);
            Stop toStop = stopService.getStopByName(toName);

            if (fromStop == null || toStop == null) {
                context.status(404).result("Stop not found.");
                return;
            }

            ArrayList<Route> routes = routeService.getRouteBetweenStops(
                    fromStop.getId(), toStop.getId()
            );


            if (routes.isEmpty()) {
                context.status(404).result("No direct connection.");
                return;
            }

            Route route = routes.get(0);

            //Parse optional time
            String timeParam = context.formParam("time");
            LocalTime userTime = null;

            if (timeParam!= null && !timeParam.isBlank()) {
                try {
                    userTime = LocalTime.parse(timeParam);
                } catch (Exception e) {
                    context.status(400).result("Invalid time.");
                }
            }

            // Calculate distance
            double distance = routeService.calculateDistanceBetweenStops(fromStop, toStop);

            //Get timetable for route
            Timetable timetable = timetableService.getTimetableForRoute(route.getId());
            if (timetable == null) {
                context.status(404).result("No timetable found.");
                return;
            }

            //Next departure
            LocalTime departureTime = timetableService.getSubscribedTour(timetable.getId(), userTime);
            String departure = departureTime != null ? departureTime.toString() : "No more departures today";

            int travelTimeMinutes = timetableService.timeBetweenStops(route, fromStop, toStop);

            String arrival;
            long durationMinutes = 0L;

            if (departureTime != null) {
                LocalTime arrivalTime = departureTime.plusMinutes(travelTimeMinutes);
                arrival = arrivalTime.toString();

                long differanceBetweenDeptAndArrival = java.time.Duration.between(departureTime, arrivalTime).toMinutes();
                if (differanceBetweenDeptAndArrival < 0) differanceBetweenDeptAndArrival  += 24 * 60;
                durationMinutes = differanceBetweenDeptAndArrival;
            } else {
                arrival = "No arrivals today";
                durationMinutes = 0L;
            }

            context.json(Map.of(
                    "route", fromName + " → " + toName,
                    "distance", distance,
                    "departure", departure,
                    "arrival", arrival,
                    "duration", durationMinutes + " min",
                    "type", route.getType(),
                    "timetableId", timetable.getId()));


        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Server error: " + e.getMessage());
        }
    }

    /**
     * Creates a new route with selected stops.
     * Expected form parameters:
     * <li>{@code name} - route name</li>
     * <li>{@code stopIds[]} - list of stop Ids</li>
     * @param context Javalin HTTP request context
     */
    public void addRoute(Context context) {
        try {

            String name = context.formParam("name");
            String[] stopIds = context.formParams("stopIds").toArray(new String[0]);

            ArrayList<Stop> stops = new ArrayList<>();
            for (String idStr : stopIds) {
                Stop stop = stopService.getStopById(Integer.parseInt(idStr));
                if (stop != null)
                    stops.add(stop);
            }

            Route newRoute = new Route(0, name, stops, null);
            routeService.addRoute(newRoute);

            context.status(201).result("Route added");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error adding route" + e.getMessage());
        }
    }

    /**
     * Updates an existing route using route ID from the URL.
     * Updates its name and stop list
     *
     * @param context Javalin HTTP request context
     */
    public void updateRoute(Context context) {
        try {

            int id = Integer.parseInt(context.pathParam("id"));
            String name = context.formParam("name");
            String[] stopIds = context.formParams("stopIds").toArray(new String[0]);

            Route oldRoute = routeService.getRouteById(id);
            if (oldRoute == null) {
                context.status(404).result("Route not found");
                return;
            }

            ArrayList<Stop> stops = new ArrayList<>();
            for (String idStr : stopIds) {
                Stop stop = stopService.getStopById(Integer.parseInt(idStr));
                if (stop != null) {
                    stops.add(stop);
                }
            }

            Route newRoute = new Route(id, name, stops, null);
            routeService.updateRoute(oldRoute, newRoute);

            context.result("Route updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error updating route: " + e.getMessage());
        }
    }

    /**
     * Deletes a route by ID. If the route does not exist, returns 404.
     *
     * @param context Javalin HTTP request context
     */

    public void deleteRoute(Context context) {
        try {

            int id = Integer.parseInt(context.pathParam("id"));
            Route route = routeService.getRouteById(id);

            if (route == null) {
                context.status(404).result("Route not found");
                return;
            }

            routeService.deleteRoute(route);
            context.status(204).result("Route deleted");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error deleting route" + e.getMessage());
        }
    }
}

