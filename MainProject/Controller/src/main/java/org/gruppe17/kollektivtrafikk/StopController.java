package org.gruppe17.kollektivtrafikk;


import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.service.StopService;

import java.util.List;

/**
 *
 * Provides CRUD endpoints for managing stops in admin panel.
 * Uses StopService to communicate with the database.
 */

public class StopController {

    public static void register(Javalin app) {


        // Get all stops
        app.get("/admin/stops", context -> {
            List<Stop> stops = StopService.getAllStops();
            context.json(stops);
        });

        // Add a stop
        app.post("/admin/stops", context -> {
            try {
                String name = context.formParam("name");
                String town = context.formParam("town");
                float lat = Float.parseFloat(context.formParam("lat"));
                float lon = Float.parseFloat(context.formParam("lon"));
                boolean roof = Boolean.parseBoolean(context.formParam("roof"));
                boolean accessibility = Boolean.parseBoolean(context.formParam("accessibility"));

                Stop newStop = new Stop(0, name, town, lat, lon, roof, accessibility);
                StopService.addStop(newStop);

                context.status(201).result("Stop added");
            } catch (Exception e) {
                context.status(400).result("Invalid stop data");
            }
        });

        // Update stop
        app.put("/admin/stops/{id}", context -> {
            try {
                int id = Integer.parseInt(context.pathParam("id"));
                Stop existing = StopService.getStopById(id);

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
                StopService.updateStop(existing, updated);

                context.result("Stop updated");
            } catch (Exception e) {
                context.status(400).result("Could not update stop");
            }
        });

        // Delete stop
        app.delete("/admin/stops/{id}", context -> {
            try {
                int id = Integer.parseInt(context.pathParam("id"));
                Stop stop = StopService.getStopById(id);

                if (stop == null) {
                    context.status(404).result("Stop not found");
                    return;
                }

                StopService.deleteStop(stop);
                context.result("Stop deleted");
            } catch (Exception e) {
                context.status(400).result("Could not delete stop");
            }
        });

    }
}
