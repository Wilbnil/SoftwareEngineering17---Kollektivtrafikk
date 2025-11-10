package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.RepositoryStop;

import java.util.ArrayList;
import java.util.List;

public class StopService {
    private RepositoryStop stopRepository;

    // Accepts a StopRepository when a StopService object is created
    public StopService(RepositoryStop stopRepository) {
        this.stopRepository = stopRepository;
    }

    // Returns all bus stops from the database
    public List<Stop> getAllStops() {
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

    // Returns all stops that has roofs
    public List<Stop> getStopsWithRoof() {
        try {
            // Gets all the stops from the repository
            List<Stop> allStops = stopRepository.getAll();
            // A list that is going to hold the stops that includes a roof
            List<Stop> stopsWithRoof = new ArrayList<>();

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
    public List<Stop> getAccessibleStops() {
        try {
            // Gets all the stops from the repository
            List<Stop> allStops = stopRepository.getAll();
            // A list that is going to hold the stops that is accessible
            List<Stop> accessibleStops = new ArrayList<>();

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
}
