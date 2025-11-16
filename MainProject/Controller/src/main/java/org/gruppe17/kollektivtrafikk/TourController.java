package org.gruppe17.kollektivtrafikk;

import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.service.TimetableService;

import java.time.LocalTime;
import java.util.ArrayList;


public class TourController {

    private TimetableService timetableService;
    private FrontEndControllerAdmin adminFront;

    public TourController(TimetableService timetableService, FrontEndControllerAdmin adminFront) {
        this.timetableService = timetableService;
        this.adminFront = adminFront;
    }

    public void getAll(Context context) {
        try {
            ArrayList<Timetable> timetable = timetableService.getAllTimetables();
            context.json(timetable);
        } catch (Exception e){
            context.status(500).result("Error fetching timetables" + e.getMessage());
        }
    }

    public void add(Context context) {
        try {
            adminFront.requireAdmin(context);
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
            adminFront.requireAdmin(context);
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
            adminFront.requireAdmin(context);
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
