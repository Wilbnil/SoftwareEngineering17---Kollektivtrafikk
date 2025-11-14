package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.StopRepository;

import java.util.ArrayList;

public class StopService {
    private StopRepository stopRepository;

    // Accepts a StopRepository when a StopService object is created
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    // Returns all bus stops from the database
    public ArrayList<Stop> getAllStops() {
        try {
            // Gets all bus stops from the repository
            return stopRepository.getAll();
        } catch (Exception e) {
            // Catches in case anything goes wrong
            System.err.println(e.getMessage());
            // Returns an empty ArrayList if unable to connect to the repository
            return new ArrayList<>();
        }
    }

    // Returns a stop by its name
    public Stop getStopByName(String name) {
        try {
            return stopRepository.getByName(name);
        } catch (Exception e) {
            // Catches in case anything goes wrong
            System.err.println(e.getMessage());
            // Returns null if no stop gets found
            return null;
        }
    }

    // Returns a stop by its id
    public Stop getStopById(int id) {
        try {
            return stopRepository.getById(id);
        } catch (Exception e) {
            // Catches in case anything goes wrong
            System.err.println(e.getMessage());
            // Returns null if no stop gets found
            return null;
        }
    }

    // Returns all stops that has roofs
    public ArrayList<Stop> getStopsWithRoof() {
        try {
            // Gets all the stops from the repository
            ArrayList<Stop> allStops = stopRepository.getAll();
            // A list that is going to hold the stops that includes a roof
            ArrayList<Stop> stopsWithRoof = new ArrayList<>();

            // Goes through every stop and adds roof included to stopsWithRoof
            for (Stop stop : allStops) {
                if (stop.getRoof()) {
                    stopsWithRoof.add(stop);
                }
            }
            // Returns an ArrayList of bus stops with roofs
            return stopsWithRoof;
        } catch (Exception e) {
            // Catches in case anything goes wrong
            System.err.println(e.getMessage());
            // Returns an empty ArrayList if unable to connect to the repository
            return new ArrayList<>();
        }
    }

    // Returns all stops that is accessible
    public ArrayList<Stop> getAccessibleStops() {
        try {
            // Gets all the stops from the repository
            ArrayList<Stop> allStops = stopRepository.getAll();
            // A list that is going to hold the stops that is accessible
            ArrayList<Stop> accessibleStops = new ArrayList<>();

            // Goes through every stop and adds accessible stops to accessibleStops
            for (Stop stop : allStops) {
                if (stop.getAccessibility()) {
                    accessibleStops.add(stop);
                }
            }
            // Returns an ArrayList of bus stops that is accessible
            return accessibleStops;
        } catch (Exception e) {
            // Catches in case anything goes wrong
            System.err.println(e.getMessage());
            // Returns an empty ArrayList if unable to connect to the repository
            return new ArrayList<>();
        }
    }

    // Adds a stop to the database
    public void addStop(Stop stop, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to add stops.");
        }
        stopRepository.insert(stop);
    }

    // Updates a stop in the database
    public void updateStop(Stop oldStop, Stop newStop, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to update stops.");
        }
        stopRepository.update(oldStop, newStop);
    }

    // Deletes a stop from the database
    public void deleteStop(Stop stop, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to delete stops.");
        }
        stopRepository.delete(stop);
    }
}