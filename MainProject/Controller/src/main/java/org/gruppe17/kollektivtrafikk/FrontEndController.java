package org.gruppe17.kollektivtrafikk;

import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.StopData;
import org.gruppe17.kollektivtrafikk.service.RouteServiceImpl;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.time.LocalDateTime;
import java.util.Map;

public class FrontEndController {
    public static void main(String[] args) {
        // Create Javalin server with static files
        // Serve files from /public folder in the classpath
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {staticFiles.directory = "/public"; staticFiles.location = Location.CLASSPATH;});
        }).start(8080); // Start server on port 8080

        // Redirect root "/" to index.html
        app.get("/", context -> context.redirect("index.html"));

        //  Define POST endpoint "/search"
        app.post("/search", context -> {
            try {
                // Get form parameters
                String from = context.formParam("from");
                String to = context.formParam("to");

                // Return 400 if no parameters
                if (from == null && to == null) {
                    context.status(400).result("No data");
                    return;
                }
                //  Use RouteService to get route
                    RouteService service = new RouteServiceImpl();
                    Route route = service.getRoute(from, to);
                //  Return 404 if route not found
                    if (route == null) {
                        context.status(404).result("Route not found");
                        return;
                    }

                // Calculate additional info
                    double distance = DistanceCalculator.getDistanceFromStops(from, to);
                    LocalDateTime departure = StopData.getDepartureTime(from);
                    LocalDateTime arrival = StopData.getArrivalTime(from, to);

                // Return JSON response
                context.json(Map.of(
                            "route", from + " to " + to,
                            "distance", distance,
                            "departure", departure != null ? departure.toString() : "N/A",
                            "arrival", arrival != null ? arrival.toString() : "N/A",
                            "bus", route.getMode() != null ? route.getMode() : "N/A"
                    ));
                } catch (Exception e) {
                //  Handle server errors --
                    e.printStackTrace();
                context.status(500).result("Server error: " + e.getMessage());
            }
        });
    }
}
