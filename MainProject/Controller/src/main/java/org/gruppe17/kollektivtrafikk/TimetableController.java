package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.service.TimetableService;

public class TimetableController {

    public static void register(Javalin app, TimetableRepository repo) {


        //get all timetables
        app.get("/timetables", context -> {
            context.json(TimetableService.getAllTimetables());
        });

        //add timetables
        app.post("/timetables", context -> {

            String routeIdStr = context.formParam("route_id");
            String day = context.formParam("day");
            String firstTime = context.formParam("first_time");
            String lastTime = context.formParam("last_time");
            String intervalStr = context.formParam("interval");

            if (routeIdStr == null || day == null || firstTime == null || lastTime == null || intervalStr == null) {
                context.status(400).result("Missing input");
                return;
            }

            int routeId = Integer.parseInt(routeIdStr);
            int interval = Integer.parseInt(intervalStr);

            Timetable t = new Timetable(0, routeId, day, firstTime, lastTime, interval);
            TimetableService.addTimetable(t);
                context.status(201).result("Timetable added");
        });

        //deletes timetiables
        app.delete("/timetables/{id}", context -> {
            int id = Integer.parseInt(context.pathParam("id"));
            TimetableService.deleteTimetable(id);
            context.status(200).result("Timetable deleted");
        });

    }
}
