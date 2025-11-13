package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.RouteRepository;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.utility.DatabaseUtility;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;


import java.util.ArrayList;
import java.util.List;

public class RouteService {
    private RouteRepository routeRepository;
    private TimetableRepository timetableRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    //gets all the routes
    public  List<Route> getAllRoutes() {
        try {
            return routeRepository.getAll();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public Route getRouteById(int id) {
        try {
            return routeRepository.getById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Route> getRouteBetweenStops(int fromStopId, int toStopId) {
       try {
           return routeRepository.getAllFromTo(fromStopId, toStopId);
       } catch (Exception e) {
           System.err.println(e.getMessage());
           return new ArrayList<>();
       }
    }

    public double calculateDistanceBetweenStops(Stop fromStop, Stop toStop) {
        if (fromStop == null || toStop == null) return 0.0;

        return DistanceCalculator.getDistance(
                fromStop.getLongitude(), fromStop.getLatitude(),
                toStop.getLongitude(), toStop.getLatitude()
        );
    }

    // Adds a route to the database
    public void addRoute(Route route) {
        try {
            routeRepository.insert(route);
            routeRepository.insertRouteStops(route);
            System.out.println("Route added successfully with stops.");
        } catch (Exception e) {
            System.err.println("Error adding route: " + e.getMessage());
        }
    }

    //updates a route in databse
    public void updateRoute(Route oldRoute, Route newRoute) {
        DatabaseUtility dbUtil = new DatabaseUtility();
        try {
            if (dbUtil.getStopIdsFromStops(oldRoute.getStops()) == dbUtil.getStopIdsFromStops(newRoute.getStops())) {
                routeRepository.deleteRouteStops(oldRoute);
                timetableRepository.deleteRouteStopTime(oldRoute);
                routeRepository.update(oldRoute, newRoute);
                routeRepository.insertRouteStops(newRoute);
            } else {
                routeRepository.update(oldRoute, newRoute);
            }
        } catch (Exception e) {
            System.err.println("Error updating route: " + e.getMessage());
        }
    }

    // Deletes a route from the database
    public void deleteRoute(Route route) {
        try {
            routeRepository.deleteRouteStops(route);
            timetableRepository.deleteTimetablesInRoute(route);
            timetableRepository.deleteRouteStopTime(route);

            routeRepository.delete(route);
            System.out.println("Route deleted successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
