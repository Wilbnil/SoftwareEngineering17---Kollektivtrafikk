package org.gruppe17.kollektivtrafikk.model;

import java.util.ArrayList;

//Represents a transport route with a list of stops and a transport mode.

public class Route {

    private int id;
    private ArrayList<Stop> stops;
    private String mode;

    public Route(ArrayList<Stop> stops, String mode) {
        this.stops = stops;
        this.mode = mode;
    }

    //Return the index of the first stop, or -1 if stops is null or empty
    public Stop getStartStop() {
        return stops != null && !stops.isEmpty() ? stops.get(0) : null;
    }

    //Return the index of the last stop, or -1 if stops is null or empty
    public Stop getEndStop() {
        return stops != null && !stops.isEmpty() ? stops.get(stops.size() -1) : null;
    }

    public String getMode() {
        return mode;
    }

    public void setId(int id) {
        this.id = id;
    }
}
