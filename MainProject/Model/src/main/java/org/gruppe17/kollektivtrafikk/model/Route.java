package org.gruppe17.kollektivtrafikk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Represents a transport route with a list of stops and a transport mode.

public class Route {

    private int id;
    private String name;
    private List<Stop> stops;
    private String mode;

    public Route(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Route(int id, String name, List<Stop> stops) {
        this.id = id;
        this.name = name;
        this.stops = new ArrayList<>(stops);
    }

    public Route(int id, String name, List<Stop> stops, String mode) {
        this.id = id;
        this.name = name;
        this.stops = new ArrayList<>(stops);
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public String getMode() {
        return mode;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    //Return the index of the first stop, or -1 if stops is null or empty
    public Stop getStartStop() {
        return stops != null && !stops.isEmpty() ? stops.get(0) : null;
    }

    //Return the index of the last stop, or -1 if stops is null or empty
    public Stop getEndStop() {
        return stops != null && !stops.isEmpty() ? stops.get(stops.size() -1) : null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Stop> getStopsWithRoof() {
        if (stops == null) return new ArrayList<>();
        return stops.stream()
                .filter(Stop::getRoof)
                .collect(Collectors.toList());
    }

    public List<Stop> getDisabilityStops() {
        if (stops == null) return new ArrayList<>();
        return stops.stream()
                .filter(Stop::getDisabilityAccess)
                .collect(Collectors.toList());
    }

}
