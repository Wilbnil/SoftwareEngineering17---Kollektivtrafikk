package org.gruppe17.kollektivtrafikk;


import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;


import java.util.ArrayList;


public class RouteController {

    private RouteService routeService;
    private StopService stopService;
    private FrontEndControllerAdmin adminFront;

    public RouteController(RouteService routeService, StopService stopService, FrontEndControllerAdmin adminFront) {
        this.routeService = routeService;
        this.stopService = stopService;
        this.adminFront = adminFront;
    }


    public void getAllRoutes(Context context) {
        ArrayList<Route> routes = routeService.getAllRoutes();
        context.json(routes);
    }

    public void addRoute(Context context) {
        try {
            adminFront.requireAdmin(context);
            String name = context.formParam("name");
            String[] stopIds = context.formParams("stopIds").toArray(new String[0]);

            ArrayList<Stop> stops = new ArrayList<>();
            for (String idStr : stopIds) {
                Stop stop = stopService.getStopById(Integer.parseInt(idStr));
                if (stop != null)
                    stops.add(stop);
            }

            Route newRoute = new Route(0, name, stops, null);
            routeService.addRoute(newRoute);

            context.status(201).result("Route added");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error adding route" + e.getMessage());
        }
    }

    public void updateRoute(Context context) {
        try {
            adminFront.requireAdmin(context);
            int id = Integer.parseInt(context.pathParam("id"));
            String name = context.formParam("name");
            String[] stopIds = context.formParams("stopIds").toArray(new String[0]);

            Route oldRoute = routeService.getRouteById(id);
            if (oldRoute == null) {
                context.status(404).result("Route not found");
                return;
            }

            ArrayList<Stop> stops = new ArrayList<>();
            for (String idStr : stopIds) {
                Stop stop = stopService.getStopById(Integer.parseInt(idStr));
                if (stop != null) {
                    stops.add(stop);
                }
            }

            Route newRoute = new Route(id, name, stops, null);
            routeService.updateRoute(oldRoute, newRoute);

            context.result("Route updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error updating route: " + e.getMessage());
        }
    }

    public void deleteRoute(Context context) {
        try {
            adminFront.requireAdmin(context);
            int id = Integer.parseInt(context.pathParam("id"));
            Route route = routeService.getRouteById(id);

            if (route == null) {
                context.status(404).result("Route not found");
                return;
            }

            routeService.deleteRoute(route);
            context.status(204).result("Route deleted");

        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Error deleting route" + e.getMessage());
        }
    }
}

