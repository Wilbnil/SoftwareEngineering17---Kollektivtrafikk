package org.gruppe17.kollektivtrafikk.model;

import java.util.ArrayList;

//Represents a transport route with a list of stops and a transport mode.

public class Route {

    private int id;
    private String name;
    private ArrayList<Stop> stops;
    private String type;

    public Route(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Route(String name, ArrayList<Stop> stops, String type) {
        this.name = name;
        this.stops = stops;
        this.type = type;
    }

    public Route(int id, String name, ArrayList<Stop> stops, String type) {
        this.id = id;
        this.name = name;
        this.stops = stops;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Stop> getStops() {
        return new ArrayList<>(stops);
    }

    public String getType() {
        return type;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    //Return the index of the first stop, or -1 if stops is null or empty
    public Stop getStartStop() {
        return stops.get(0);
    }

    //Return the index of the last stop, or -1 if stops is null or empty
    public Stop getEndStop() {
        return stops.get(stops.size() - 1);
    }

    public void setId(int id) {
        this.id = id;
    }
}
