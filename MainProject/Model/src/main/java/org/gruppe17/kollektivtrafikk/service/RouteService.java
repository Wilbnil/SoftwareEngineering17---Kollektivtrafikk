package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.RouteRepository;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.utility.DatabaseUtility;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;


import java.util.ArrayList;

/**
 * The {@code RouteService} class handles all the logic related stuff for the bus stops in the public transport system.
 *
 * <p>
 * This service class provides methods like retrieving, adding, updating and deleting routes.
 * It includes as well, methods that finds routes between stops, calculating distances and
 * managing route stop relations.
 * This class interacts with {@code RouteRepository} and {@code TimetableRepository} in order to do its database operations.
 * </p>
 *
 * <p>
 * This service uses {@code DistanceCalculator} to calculate distance between stops.
 * </p>
 *
 * <p>
 * Example usage:
 * <blockquote><pre>
 * ArrayList<Route> routes = routeService.getAllRoutes();
 * ArrayList<Route> routesBetween = routeService.getRouteBetweenStops(1, 5);
 * double distance = routeService.calculateDistanceBetweenStops(stopA, stopB);
 * </pre></blockquote>
 * </p>
 *
 * <p>
 * {@code RouteService} should be instantiated with {@code RouteRepository} and {@code TimetableRepository}.
 * </p>
 */
public class RouteService {
    private RouteRepository routeRepository;
    private TimetableRepository timetableRepository;

    /**
     * Creates a new RouteService with the repositories.
     *
     * @param routeRepository The repository used
     * @param timetableRepository The repository used
     */
    public RouteService(RouteRepository routeRepository, TimetableRepository timetableRepository) {
        this.routeRepository = routeRepository;
        this.timetableRepository = timetableRepository;
    }

    /**
     * Gets all the routes from the database.
     *
     * @return An ArrayList with all the routes, or an empty ArrayList if it fails
     */
    public  ArrayList<Route> getAllRoutes() {
        try {
            return routeRepository.getAll();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gets a route by its id
     *
     * @param id The identifier of the route
     * @return The route if found, or null otherwise
     */
    public Route getRouteById(int id) {
        try {
            return routeRepository.getById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets all routes that is between stops.
     *
     * @param fromStopId The identifier of the departure stop
     * @param toStopId The identifier of the arrival stop
     * @return An ArrayList of routes that connect those two stops, otherwise an empty ArrayList if nothing is found.
     */
    public ArrayList<Route> getRouteBetweenStops(int fromStopId, int toStopId) {
       try {
          return routeRepository.getAllFromTo(fromStopId, toStopId);
       } catch (Exception e) {
           System.err.println(e.getMessage());
           return new ArrayList<>();
       }
    }

    /**
     * A method that calculate the distance in kilometers between two stops.
     * <p>
     * This method uses latitude and longitude of the both stops to calculate
     * the distance.
     * </p>
     *
     * @param fromStop The departure stop
     * @param toStop The arrival stop
     * @return The distance in kilometers, otherwise 0.0 if either stop is null
     */
    public double calculateDistanceBetweenStops(Stop fromStop, Stop toStop) {
        if (fromStop == null || toStop == null) return 0.0;

        double degrees = DistanceCalculator.getDistance(fromStop.getLongitude(), fromStop.getLatitude(), toStop.getLongitude(), toStop.getLatitude());
        double km = degrees * 111;
        return Math.round(km * 100.0) / 100.0;
    }

    /**
     * Adds a route to the database with its stops.
     *
     * @param route The Route that is getting added, including its stops
     */
    public void addRoute(Route route) {
        try {
            routeRepository.insert(route);
            routeRepository.insertRouteStops(route);
            System.out.println("Route added successfully with stops.");
        } catch (Exception e) {
            System.err.println("Error adding route: " + e.getMessage());
        }
    }

    /**
     * Updates a route in the database.
     * <p>
     * If the list of stops has changed, it deletes and recreates the relationships between route-stop and timetable.
     * Otherwise, it only updates the route information.
     * </p>
     *
     * @param oldRoute The Route that is about to be updated
     * @param newRoute The new Route with the updated information
     */
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

    /**
     * Deletes a route from the database
     * <p>
     * This method removes the route, stop relationships, timetables, and the info about the route-stop times
     * from the database.
     * </p>
     *
     * @param route The Route that is going to be deleted
     */
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
