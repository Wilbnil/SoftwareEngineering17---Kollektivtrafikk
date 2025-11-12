package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.repository.RepositoryTimetable;
import org.gruppe17.kollektivtrafikk.service.TimetableService;

import java.sql.Connection;

public class TimetableController {

    public static void register(Javalin app, Connection connection) {

        RepositoryTimetable timetableRepo = new RepositoryTimetable(connection);
        TimetableService.init(timetableRepo);

        //get all timetables
        app.get("/timetables", context -> {
            try {
                List<Timetable> timetables = TimetableService.getAllTimetables();
                context.json(timetables);
            } catch (Exception e) {
                e.printStackTrace();
                context.status(500).result("Error loading timetables" + e.getMessage());
            }
        });

        //add timetables
        app.post("/timetables", context -> {
            try {
                int routeId = Integer.parseInt(context.formParam("route_id"));
                String day = context.formParam("day");
                String firstTime = context.formParam("first_time");
                String lastTime = context.formParam("last_time");
                int interval = Integer.parseInt(context.formParam("interval"));

                Timetable newTable = new Timetable(0, routeId, day, firstTime, lastTime, interval)
                TimetableService.addTimetable(newTable);

                context.status(201).result("Timetable added successfully");
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Error loading timetables" + e.getMessage());
            }
        });

        //deletes timetable
        app.delete("/timetables/{id}", context -> {
            try {
                int id = Integer.parseInt(context.pathParam("id"));
                TimetableService.deleteTimetable(id);

                context.status(200).result("Timetable deleted");
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Error deleting timetables" + e.getMessage());
            }
        });

    }
}
