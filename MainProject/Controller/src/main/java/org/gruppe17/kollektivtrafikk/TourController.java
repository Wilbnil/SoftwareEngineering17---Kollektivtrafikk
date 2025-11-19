package org.gruppe17.kollektivtrafikk;

import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.model.Tour;
import org.gruppe17.kollektivtrafikk.service.TimetableService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * The {@code TourController} class handles all HTTP endpoints related to tours
 * and timetables. It acts as the interface between the frontend and the {@link TimetableService},
 * allowing clients to request upcoming tours,
 * notifications, and perform CRUD operations on timetables.
 *
 * <p>This controller supports:</p>
 * <li>Fetching all tours for the current day</li>
 * <li>Getting arrival notifications for specific timetable</li>
 * <li>Adding timetables</li>
 * <li>Updating timetables</li>
 * <li>Deleting timetables</li>
 *
 * Correct Usage Example:
 * <blockquote><pre>
 *     app,get("/api/tours", tourController::getAll);
 *     app.get("/api/tours/notify", tourController::getNotification);
 * </pre></blockquote>
 *
 * Incorrect Usage Example:
 * <blockquote><pre>
 *     new TourController(null).add(null);
 * </pre></blockquote>
 */
public class TourController {

    private TimetableService timetableService;


    /**
     * Constructs a new {@code TourController} with the required service dependency.
     *
     * @param timetableService the service layer responsible for timetable and tour log
     */

    public TourController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    /**
     * Return all tours for today's date as JSON.
     * A tour represents a route with a list of arrival times generated
     * from its timetable definition.
     *
     * @param context Javalin HTTP request context
     */
    public void getAll(Context context) {
        try {
            ArrayList<Tour> tours = timetableService.getAllTours();
            context.json(tours);
        } catch (Exception e){
            context.status(500).result("Error fetching timetables" + e.getMessage());
        }
    }

    /**
     * Returns a notification message for the next upcoming vehicle arrival
     * for the given timetable.
     *
     * <p>Required query parameter:</p>
     * <li>{@code timetableId} - the ID of the timetable</li>
     * Example response:
     * <pre>
     *     { "notification": "The bus arrives in 7 minutes"}
     * </pre>
     *
     * @param context Javalin request context
     */

    public void getNotification (Context context) {
        try {
            String raw = context.queryParam("timetableId");

            if (raw == null) {
                context.status(400).json(Map.of("error", "Missing timetableId"));
                return;
            }
            int timetableId = Integer.parseInt(raw);

            String message = timetableService.notification(timetableId);

            context.json(Map.of("notification", message));

        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).json(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Adds a new timetable using form parameters provided in the request.
     *
     * <p>Expected parameters:</p>
     * <li>{@code route_id} - ID of the associated route</li>
     * <li>{@code day} - day of the week (e.g. monday)</li>
     * <li>{@code first_time} - first departure time</li>
     * <li>{@code last_time} - last departure time</li>
     * <li>{@code interval} - the interval between departures</li>
     *
     * @param context Javalin request context
     */

    public void add(Context context) {
        try {

            int routeId = Integer.parseInt(context.formParam("route_id"));
            String day_of_week = context.formParam("day");
            LocalTime firstTime = LocalTime.parse(context.formParam("first_time"));
            LocalTime lastTime = LocalTime.parse(context.formParam("last_time"));
            int interval = Integer.parseInt(context.formParam("interval"));

            Timetable timetable = new Timetable(0, routeId, day_of_week, firstTime, lastTime, interval);
            timetableService.addTimetable(timetable, true);

            context.status(201).result("Timetable added.");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error adding timetables" + e.getMessage());
        }
    }

    /**
     * Uodates an existing timetable. The timetable ID is provided via path parameter,
     * and the updated data is provided as form parameters.
     *
     * <p>If the timetable does not exist, returns HTTP 404.</p>
     *
     * @param context Javalin HTTP request context
     */

    public void update(Context context) {
        try {

            int id = Integer.parseInt(context.pathParam("id"));
            int routeId = Integer.parseInt(context.formParam("route_id"));
            String day_of_week = context.formParam("day");
            LocalTime firstTime = LocalTime.parse(context.formParam("first_time"));
            LocalTime lastTime = LocalTime.parse(context.formParam("last_time"));
            int interval = Integer.parseInt(context.formParam("interval"));

            Timetable oldTimetable = timetableService.getTimetableById(id);
            if (oldTimetable == null) {
                context.status(404).result("Timetable not found.");
                return;
            }

            Timetable newTimetable = new Timetable(id, routeId, day_of_week, firstTime, lastTime, interval);
            timetableService.updateTimetable(oldTimetable, newTimetable, true);

            context.result("Timetable updated.");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error updating timetable: " + e.getMessage());
        }
    }

    /**
     * Deletes a timetable based on the ID provided in the URL path.
     *
     * <p>If the timetable does not exist, return HTTP 404.</p>
     *
     * @param context Javalin HTTP request context.
     */

    public void delete(Context context) {
        try {

            int id = Integer.parseInt(context.pathParam("id"));
            Timetable timetable = timetableService.getTimetableById(id);
            if (timetable == null) {
                context.status(404).result("No timetable found.");
                return;
            }
            timetableService.deleteTimetable(timetable, true);

            context.result("Timetable deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error deleting timetables" + e.getMessage());
        }
    }

}
