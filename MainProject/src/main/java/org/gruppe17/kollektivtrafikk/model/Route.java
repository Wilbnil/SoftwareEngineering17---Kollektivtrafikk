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

    public int getStartStop() {
        return 0;
    }

    public int getEndStop() {
        return 0;
    }

}
