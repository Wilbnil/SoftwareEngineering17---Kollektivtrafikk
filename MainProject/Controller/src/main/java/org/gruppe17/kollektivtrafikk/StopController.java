package org.gruppe17.kollektivtrafikk;


import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.RepositoryStop;
import org.gruppe17.kollektivtrafikk.service.StopService;

import java.sql.Connection;
import java.util.List;

/**
 *
 * Provides CRUD endpoints for managing stops in admin panel.
 * Uses StopService to communicate with the database.
 */

public class StopController {

    public static void register(Javalin app, StopService stopService) {

        // Get all stops
        app.get("/admin/stops", context -> {
            List<Stop> stops = stopService.getAllStops();
            context.json(stops);
        });

        // Post - Add a stop
        app.post("/admin/stops", context -> {
            try {
                String name = context.formParam("name");
                String town = context.formParam("town");
                float lat = Float.parseFloat(context.formParam("lat"));
                float lon = Float.parseFloat(context.formParam("lon"));
                boolean roof = Boolean.parseBoolean(context.formParam("roof"));
                boolean accessibility = Boolean.parseBoolean(context.formParam("accessibility"));

                if (name == null || name.isBlank() || town == null || town.isBlank()) {
                    context.status(400).result("Missing required fields");
                    return;
                }

                Stop newStop = new Stop(0, name, town, lat, lon, roof, accessibility);

                boolean ok = stopService.addStop(newStop);
                if (ok) {
                    context.status(201).result("Stop added successfully");
                } else {
                    context.status(500).result("Error adding stop");
                }

            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Invalid stop data" + e.getMessage());
            }
        });

        // Put - Update stop
        app.put("/admin/stops/{id}", context -> {
            try {
                int id = Integer.parseInt(context.pathParam("id"));
                Stop existing = stopService.getStopById(id);

                if (existing == null) {
                    context.status(404).result("Stop not found");
                    return;
                }

                String name = context.formParam("name");
                String town = context.formParam("town");
                float lat = Float.parseFloat(context.formParam("lat"));
                float lon = Float.parseFloat(context.formParam("lon"));
                boolean roof = Boolean.parseBoolean(context.formParam("roof"));
                boolean accessibility = Boolean.parseBoolean(context.formParam("accessibility"));

                Stop updated = new Stop(id, name, town, lat, lon, roof, accessibility);

                boolean ok = stopService.updateStop(existing, updated);

                if (ok) {
                    context.result("Stop updated successfully");
                } else {
                    context.status(500).result("Error updating stop");
                }
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Could not update stop" + e.getMessage());
            }
        });

        // Delete a stop
        app.delete("/admin/stops/{id}", context -> {
            try {
                int id = Integer.parseInt(context.pathParam("id"));
                Stop stop = stopService.getStopById(id);

                if (stop == null) {
                    context.status(404).result("Stop not found");
                    return;
                }

                boolean ok = stopService.deleteStop(stop);
                if (ok) {
                    context.result("Stop deleted successfully");
                } else {
                    context.status(500).result("Error deleting stop");
                }
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Could not delete stop" + e.getMessage());
            }
        });

    }
}
