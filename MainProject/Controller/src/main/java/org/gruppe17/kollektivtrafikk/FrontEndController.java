package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.repository.DatabaseSQLAdapter_OLD;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.service.TimetableService;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.RouteService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
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

    /**
     * Register endpoints on the given Javalin app.
     * @param app       running Javalin instance (created in Application)
     * @param dbAdapter initialized adapter (constructed in Application with a JDBC Connection)
     * @param conn      open JDBC connection (from Application)
     */
    public static void register(Javalin app, DatabaseSQLAdapter_OLD dbAdapter, Connection conn) {

        // Redirect root ("/") to index.html (served as static file by Application config)
        app.get("/", context -> context.redirect("index.html"));

        // Return all stops as JSON
        app.get("/api/stops", context -> {
            try {
                StopService stopService = new StopService();
                List<Stop> stops = stopService.getAllStops();
                context.json(stops);
            } catch (Exception e) {
                e.printStackTrace();
                context.status(500).result("Error loading stops.");
            }
        });

        // Search a route between two stops and compute distance + next departure
        app.post("/search", context -> {
            try {
                String fromName = context.formParam("from");
                String toName   = context.formParam("to");

                // Basic validation
                if (fromName == null || toName == null || fromName.isBlank() || toName.isBlank()) {
                    context.status(400).result("Missing input.");
                    return;
                }
                if (fromName.equalsIgnoreCase(toName)) {
                    context.status(400).result("Stops cannot be the same.");
                    return;
                }

                // Find Stop objects by name
                StopService stopService = new StopService();
                List<Stop> allStops = stopService.getAllStops();
                Stop fromStop = null, toStop = null;
                for (Stop s : allStops) {
                    if (s.getName().equalsIgnoreCase(fromName)) fromStop = s;
                    if (s.getName().equalsIgnoreCase(toName))   toStop   = s;
                }
                if (fromStop == null || toStop == null) {
                    context.status(404).result("Stop not found.");
                    return;
                }

                // Find possible routes between these stops
                RouteService routeService = new RouteService();
                List<Route> routes = routeService.getRouteBetweenStops(fromStop.getId(), toStop.getId());


                if (routes == null || routes.isEmpty()) {
                    context.status(404).result("No route found between these stops.");
                    return;
                }

                Route route = routes.get(0); // take the first route found

                // Calculate geographic distance (km)
                double distance = DistanceCalculator.getDistance(
                        fromStop.getLongitude(), fromStop.getLatitude(),
                        toStop.getLongitude(),   toStop.getLatitude()
                );

                // Timetable
                Timetable timetable = TimetableService.getTimetableForRoute(route.getId());

                if (timetable == null) {
                    context.status(404).result("No timetable for this route.");
                    return;
                }

                String departure = "N/A";
                String arrival = "N/A";
                LocalTime now = LocalTime.now();
                LocalTime firstTime = LocalTime.parse(timetable.getFirstTime());
                LocalTime lastTime  = LocalTime.parse(timetable.getLastTime());
                int interval = timetable.getInterval();

                if (now.isBefore(firstTime)) {
                    departure = firstTime.toString();
                    arrival   = firstTime.plusMinutes(10).toString();
                } else if (now.isAfter(lastTime)) {
                    departure = "No more routes today";
                    arrival   = "N/A";
                } else {
                    long minutesSinceFirst = Duration.between(firstTime, now).toMinutes();
                    long nextBlock = ((minutesSinceFirst / interval) + 1) * interval;
                    LocalTime nextDeparture = firstTime.plusMinutes(nextBlock);
                    departure = nextDeparture.toString();
                    arrival   = nextDeparture.plusMinutes(10).toString();
                }


                // Respond with JSON
                context.json(Map.of(
                        "route",     fromName + " \u2192 " + toName, // arrow →
                        "distance",  distance,
                        "departure", departure,
                        "arrival",   arrival,
                        "mode",      route.getMode() != null ? route.getMode() : "Bus"
                ));

            } catch (Exception e) {
                e.printStackTrace();
                context.status(500).result("Server error: " + e.getMessage());
            }
        });
    }
}
