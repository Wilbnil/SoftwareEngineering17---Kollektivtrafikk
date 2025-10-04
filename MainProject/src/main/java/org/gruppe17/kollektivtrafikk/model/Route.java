package org.gruppe17.kollektivtrafikk.model;

import java.util.ArrayList;

//Represents a transport route with a list of stops and a transport mode.

public class Route {

    private ArrayList<Stop> stops;
    private String mode;

    public Route(ArrayList<Stop> stops, String mode) {
        this.stops = stops;
        this.mode = mode;
    }

    public ArrayList<Stop> getStops() { return stops; }
    public String getMode() { return mode;}

}
