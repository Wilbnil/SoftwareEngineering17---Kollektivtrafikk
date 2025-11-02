package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.gruppe17.kollektivtrafikk.db.DatabaseSQLAdapter;
import org.gruppe17.kollektivtrafikk.db.SQLiteDatabase;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;

import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

/**
 *
 * Starts Javalin server on port 8081 and connects to the SQLite database.
 * Provides 2 endpoints:
 *  - /api/stops  -> returns all stops (for suggestions in the form)
 *  - /search     -> finds a route between two stops and returns details
 */
public class FrontEndController {

    public static void main(String[] args) {
        try {
            //Connect to database
            SQLiteDatabase db = new SQLiteDatabase();
            db.startDB();
            Connection conn = db.getConnection();
            DatabaseSQLAdapter dbAdapter = new DatabaseSQLAdapter(conn);

            // Start Javalin server
            Javalin app = Javalin.create(config -> {
                config.staticFiles.add(staticFiles -> {
                    staticFiles.directory = "/public"; // folder with index.html
                    staticFiles.location = Location.CLASSPATH;
                });
            }).start(8081);

            // Redirect to index.html when opening root
            app.get("/", ctx -> ctx.redirect("index.html"));

            //  Get all stops for frontend
            app.get("/api/stops", ctx -> {
                try {
                    List<Stop> stops = dbAdapter.getAllStops();
                    ctx.json(stops);
                } catch (SQLException e) {
                    e.printStackTrace();
                    ctx.status(500).result("Error loading stops.");
                }
            });

            // Search route between two stops
            app.post("/search", ctx -> {
                try {
                    // Get form data
                    String fromName = ctx.formParam("from");
                    String toName = ctx.formParam("to");

                    if (fromName == null || toName == null) {
                        ctx.status(400).result("Missing input.");
                        return;
                    }

                    if (fromName.equalsIgnoreCase(toName)) {
                        ctx.status(400).result("Stops cannot be the same.");
                        return;
                    }

                    // Find stops in database
                    List<Stop> allStops = dbAdapter.getAllStops();
                    Stop fromStop = null;
                    Stop toStop = null;

                    for (Stop s : allStops) {
                        if (s.getName().equalsIgnoreCase(fromName)) fromStop = s;
                        if (s.getName().equalsIgnoreCase(toName)) toStop = s;
                    }

                    if (fromStop == null || toStop == null) {
                        ctx.status(404).result("Stop not found.");
                        return;
                    }

                    // Find route connecting these stops
                    ArrayList<Route> routes = DatabaseSQLAdapter.getRoutesFromDatabase(fromStop.getId(), toStop.getId());
                    if (routes.isEmpty()) {
                        ctx.status(404).result("No route found between these stops.");
                        return;
                    }

                    Route route = routes.get(0); // take first found route

                    // Calculate distance between stops
                    double distance = DistanceCalculator.getDistance(
                            fromStop.getLongitude(),
                            fromStop.getLatitude(),
                            toStop.getLongitude(),
                            toStop.getLatitude()
                    );

                    // Get timetable for this route

                    String sql = "SELECT first_time, last_time, interval FROM timetables WHERE route_id=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, route.getId());
                    ResultSet rs = stmt.executeQuery();

                    String departure = "N/A";
                    String arrival = "N/A";

                    if (rs.next()) {
                        String first = rs.getString("first_time");
                        String last = rs.getString("last_time");
                        int interval = rs.getInt("interval");

                        // time now
                        LocalTime now = LocalTime.now();

                        // conevrt text to LocalTime
                        LocalTime firstTime = LocalTime.parse(first);
                        LocalTime lastTime = LocalTime.parse(last);

                        if (now.isBefore(firstTime)) {
                            // if the bus hasn't driven yet
                            departure = firstTime.toString();
                        } else if (now.isAfter(lastTime)) {
                            // after the last course
                            departure = "No more routes today";
                        } else {
                            // find the next route
                            long minutesSinceFirst = Duration.between(firstTime, now).toMinutes();
                            long nextDepartureMinutes = ((minutesSinceFirst / interval) + 1) * interval;
                            LocalTime nextDeparture = firstTime.plusMinutes(nextDepartureMinutes);
                            departure = nextDeparture.toString();

                            // calculate travel time (f.eks 10 mins)
                            LocalTime arrivalTime = nextDeparture.plusMinutes(10);
                            arrival = arrivalTime.toString();
                        }
                    }

                    // Send route info as JSON to frontend
                    ctx.json(Map.of(
                            "route", fromName + " → " + toName,
                            "distance", distance,
                            "departure", departure,
                            "arrival", arrival,
                            "mode", route.getMode() != null ? route.getMode() : "Bus"
                    ));


                } catch (Exception e) {
                    e.printStackTrace();
                    ctx.status(500).result("Server error: " + e.getMessage());
                }
            });

            System.out.println("✅ Server started on http://localhost:8081");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
