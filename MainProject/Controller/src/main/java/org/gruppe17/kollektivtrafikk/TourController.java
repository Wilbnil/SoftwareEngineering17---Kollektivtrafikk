package org.gruppe17.kollektivtrafikk;

import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.model.Tour;
import org.gruppe17.kollektivtrafikk.service.TimetableService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;


public class TourController {

    private TimetableService timetableService;


    public TourController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    public void getAll(Context context) {
        try {
            ArrayList<Tour> tours = timetableService.getAllTours();
            context.json(tours);
        } catch (Exception e){
            context.status(500).result("Error fetching timetables" + e.getMessage());
        }
    }

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
