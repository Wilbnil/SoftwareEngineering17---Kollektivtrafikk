package org.gruppe17.kollektivtrafikk;


import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.service.StopService;

import java.util.ArrayList;


/**
 * The {@code StopController} class handles all HTTP requests related to stops.
 * It acts as the interface between the frontend and the {@link StopService} layer,
 * allowing the application to manage stop data through CRUD operations.
 * <p>This controller is responsible for:</p>
 * <li>Retrieving all stops</li>
 * <li>Adding a new stop</li>
 * <li>Updating an existing stop</li>
 * <li>Deleting a stop</li>
 *
 * Correct USage Example:
 * <blockquote><pre>
 *     app.get("/api/stops", stopController::getAllStops);
 *     app.post("/api/stops", stopController::addStop);
 * </pre></blockquote>
 *
 * Incorrect Usage Example:
 * <blockquote><pre>
 *     StopController sc = new StopController(null);
 *     sc.addStop(null);
 * </pre></blockquote>
 *
 */
public class StopController {

    private StopService stopService;


    /**
     * Creates a new {@code StopController} with the required service dependency.
     *
     * @param stopService the service resposnible for stop management
     */
    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    /**
     * Return all stops stored in the system as JSON.
     * This method does not take any parameters.
     *
     * @param context Javalin context request context
     */
      public void getAllStops(Context context) {
        ArrayList<Stop> stops = stopService.getAllStops();
        context.json(stops);
      }

    /**
     * Adds a new stop using the parameters provided via from data
     *
     * <p>Expected form parameters:</p>
     * <li>{@code name} - stop name</li>
     * <li>{@code town} - town where the stop is located</li>
     * <li>{@code lat} - latitude of the stop</li>
     * <li>{@code lon} - longitude of the stop</li>
     * <li>{@code roof} - whether the stop has a roof</li>
     * <li>{@code accessibility} - accessibility options</li>
     *
     * @param context Javalin HTTPS request context
     */
    public void addStop(Context context) {
            try {

                String name = context.formParam("name");
                String town = context.formParam("town");
                float lat = Float.parseFloat(context.formParam("lat"));
                float lon = Float.parseFloat(context.formParam("lon"));
                boolean roof = Boolean.parseBoolean(context.formParam("roof"));
                boolean accessibility = Boolean.parseBoolean(context.formParam("accessibility"));

                Stop stop = new Stop(0, name, town, lat, lon, roof, accessibility);
                stopService.addStop(stop, true);

                context.status(201).result("Stop added");

            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Error adding stop" + e.getMessage());
            }
        }

    /**
     * Updates an existing stop using the stop ID provided in the path
     * and updated fields provided via form parameters.
     *
     * <p>If the stop does not exist, return HTTP 404.</p>
     *
     * @param context Javalin HTTP request context
     */

    public void updateStop(Context context) {
            try {

                int id = Integer.parseInt(context.pathParam("id"));
                Stop oldStop = stopService.getStopById(id);

                if (oldStop == null) {
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
                stopService.updateStop(oldStop, updated, true);

                context.status(200).result("Stop updated");
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Could not update stop" + e.getMessage());
            }
        }

    /**
     * Deletes a stop based on the ID provided in the URL path.
     *
     * <p>If the stop does not exist, returns HTTP 404</p>
     * @param context
     */
    public void deleteStop(Context context) {
            try {

                int id = Integer.parseInt(context.pathParam("id"));
                Stop stop = stopService.getStopById(id);

                if (stop == null) {
                    context.status(404).result("Stop not found");
                    return;
                }
                stopService.deleteStop(stop, true);
                context.status(204).result("Stop deleted");

            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Could not delete stop" + e.getMessage());
            }
        }

}

