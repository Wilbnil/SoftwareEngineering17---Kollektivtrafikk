package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.repository.DatabaseAdminSQLAdapter_OLD;
import org.gruppe17.kollektivtrafikk.repository.DatabaseSQLAdapter_OLD;

import java.util.List;

public class RouteService {

    //USER SECTION
    public static List<Route> getAllRoutes() {
        return DatabaseSQLAdapter_OLD.getAllRoutesFromDatabase();
    }
    public static List<Route> getRouteBetweenStops(int fromStopId, int toStopId) {
        return DatabaseSQLAdapter_OLD.getRoutesFromDatabaseFromTo(fromStopId, toStopId);
    }


    // ADMIN SECTON
    // Adds a route to the database
    public static void addRoute(Route route) throws Exception {
        DatabaseAdminSQLAdapter_OLD.insertRouteIntoDatabase(route);
    }

    // Deletes a route from the database
    public static void deleteRoute(Route route) throws Exception {
        DatabaseAdminSQLAdapter_OLD.deleteRouteInDatabase(route);
    }


}
