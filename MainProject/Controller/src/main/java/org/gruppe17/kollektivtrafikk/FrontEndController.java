package org.gruppe17.kollektivtrafikk;


import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.service.TimetableService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.RouteService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;


public class FrontEndController {

    private StopService stopService;
    private RouteService routeService;
    private TimetableService timetableService;

    public FrontEndController(StopService stopService, RouteService routeService, TimetableService timetableService) {
        this.stopService = stopService;
        this.routeService = routeService;
        this.timetableService = timetableService;
    }

    public void serveHome(Context context) {
        context.redirect("index.html");
    }

    public void getAllStops(Context context) {
        ArrayList<Stop> stops = stopService.getAllStops();
        context.json(stops);
    }

    public void searchRoute(Context context) {

        try {
            String fromName = context.formParam("from");
            String toName = context.formParam("to");

            if (fromName == null || toName == null || fromName.isBlank() || toName.isBlank()) {
                context.status(400).result("Missing input.");
                return;
            }
            if (fromName.equalsIgnoreCase(toName)) {
                context.status(400).result("Stops cannot be the same.");
                return;
            }
            Stop fromStop = stopService.getStopByName(fromName);
            Stop toStop = stopService.getStopByName(toName);

            if (fromStop == null || toStop == null) {
                context.status(404).result("Stop not found.");
                return;
            }

            ArrayList<Route> routes = routeService.getRouteBetweenStops(
                    fromStop.getId(), toStop.getId()
            );


            if (routes.isEmpty()) {
                context.status(404).result("No route found.");
                return;
            }

            Route route = routes.get(0);

            double distance = routeService.calculateDistanceBetweenStops(fromStop, toStop);

            Timetable timetable = timetableService.getTimetableForRoute(route.getId());
            if (timetable == null) {
                context.status(404).result("No timetable found.");
                return;
            }

           LocalTime departureTime = timetableService.getSubscribedTour(timetable.getId());

            String departure = departureTime != null ? departureTime.toString() : "No more departures today";

            String arrival;
            if (departureTime != null) {
                int travelTimeMinutes = timetableService.timeBetweenStops(route, fromStop, toStop);
                arrival = departureTime.plusMinutes(travelTimeMinutes).toString();
            } else {
                arrival = "No arrivals today";
            }

            context.json(Map.of(
                    "route", fromName + " → " + toName,
                    "distance", distance,
                    "departure", departure,
                    "arrival", arrival,
                    "type", route.getType(),
                    "timetableId", timetable.getId()));


        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Server error: " + e.getMessage());
        }

        }

    public void getNotification(Context context) {
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

}
