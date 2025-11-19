package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.StopRepository;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;

import java.util.ArrayList;

/**
 * The {@code StopService} class handles all the logic related stuff for the bus stops in the public transport system.
 * <p>
 * This service class provides methods like retrieving, adding, updating and deleting stops.
 * It includes as well methods that filters stops with a roof or its accessibility.
 * Additionally, the service can find the nearest stops with roof or accessibility.
 * This class interacts with {@code StopRepository} in order to do its database operations.
 * </p>
 *
 * <p>
 * Only admins are able to do certain stuff like adding, updating and deleting stops.
 * </p>
 *
 * <p>
 * Example usage:
 * <blockquote><pre>
 * ArrayList<Stop> stopsWithRoof = stopService.getStopsWithRoof();
 * Stop nearestWithRoof = stopService.getNearestStopWithRoof(currentStop);
 * </pre></blockquote>
 * </p>
 *
 * <p>
 * {@code StopService} should be instantiated with {@code StopRepository}.
 * </p>
 */
public class StopService {
    private StopRepository stopRepository;

    /**
     * Creates a new StopService with StopRepository.
     *
     * @param stopRepository The repository used.
     */

    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    /**
     * Gets all the bus stops from the database.
     *
     * @return An ArrayList with all the stops or an empty ArrayList if it fails
     */
    public ArrayList<Stop> getAllStops() {
        try {
            return stopRepository.getAll();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a stop by its name.
     *
     * @param name The name of the stop
     * @return The stop if found, or null otherwise
     */
    public Stop getStopByName(String name) {
        try {
            return stopRepository.getByName(name);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets a stop by its id
     *
     * @param id The identifier of the stop
     * @return The Stop if found, or null otherwise
     */
    public Stop getStopById(int id) {
        try {
            return stopRepository.getById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets all the stops with roof.
     *
     * @return An ArrayList containing all stops with roofs, or an empty ArrayList if an error occurs
     */
    public ArrayList<Stop> getStopsWithRoof() {
        try {
            ArrayList<Stop> allStops = stopRepository.getAll();
            ArrayList<Stop> stopsWithRoof = new ArrayList<>();

            for (Stop stop : allStops) {
                if (stop.getRoof()) {
                    stopsWithRoof.add(stop);
                }
            }
            return stopsWithRoof;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gets all the stops that are accessible.
     *
     * @return An ArrayList with all accessible stops, otherwise an empty ArrayList if fails.
     */
    public ArrayList<Stop> getAccessibleStops() {
        try {
            ArrayList<Stop> allStops = stopRepository.getAll();
            ArrayList<Stop> accessibleStops = new ArrayList<>();

            for (Stop stop : allStops) {
                if (stop.getAccessibility()) {
                    accessibleStops.add(stop);
                }
            }
            return accessibleStops;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new stop to the database.
     *
     * @param stop The Stop that is going to be added
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void addStop(Stop stop, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to add stops.");
        }
        stopRepository.insert(stop);
    }

    /**
     * Updates a stop in the database.
     *
     * @param oldStop The old stop that is going to be updated
     * @param newStop The new stop that gets its new updated information
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void updateStop(Stop oldStop, Stop newStop, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to update stops.");
        }
        stopRepository.update(oldStop, newStop);
    }

    /**
     * Deletes a stop from the database.
     *
     * @param stop The Stop that is going to be deleted
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void deleteStop(Stop stop, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to delete stops.");
        }
        stopRepository.delete(stop);
    }

    /**
     * Finds the nearest replacement stop that has a roof
     *
     * @param {Stop} stop - Stop without roof
     * @return {Stop} - Nearest Stop with roof
     */
    public Stop getNearestStopWithRoof(Stop stop) {
        try {
            ArrayList<Stop> allStopsWithRoof = stopRepository.getAll();

            // Removes all stops from the ArrayList where the .getRoof() == false statement is true
            allStopsWithRoof.removeIf(stopX -> stopX.getRoof() == false);

            double shortest = 10000;
            double distance;
            int index = 0;

            // Cycles through the Stops with roof = True and finds the one with the shortest distance
            for(Stop stopX : allStopsWithRoof) {

                distance = DistanceCalculator.getDistance(stop.getLatitude(), stop.getLongitude(), stopX.getLatitude(), stopX.getLongitude());

                if(distance < shortest) {
                    shortest = distance;
                    index = allStopsWithRoof.indexOf(stopX);
                }
            }

            return allStopsWithRoof.get(index);

        } catch(Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * Finds the nearest replacement stop that has accessibility
     *
     * @param {Stop} stop - Stop without accessibility
     * @return {Stop} - Nearest Stop with accessibility
     */
    // Finds the nearest replacement stop that has accessibility help
    public Stop getNearestStopWithAccessibility(Stop stop) {
        try {
            ArrayList<Stop> allStopsWithAccessibility = stopRepository.getAll();

            // Removes all stops from the ArrayList where the .getAccessibility() == false statement is true
            allStopsWithAccessibility.removeIf(stopX -> stopX.getAccessibility() == false);

            double shortest = 10000;
            double distance;
            int index = 0;

            // Cycles through the Stops with accessibility = True and finds the one with the shortest distance
            for(Stop stopX : allStopsWithAccessibility) {

                distance = DistanceCalculator.getDistance(stop.getLatitude(), stop.getLongitude(), stopX.getLatitude(), stopX.getLongitude());

                if(distance < shortest) {
                    shortest = distance;
                    index = allStopsWithAccessibility.indexOf(stopX);
                }
            }

            return allStopsWithAccessibility.get(index);

        } catch(Exception e) {
            System.err.println(e);
        }
        return null;
    }
}