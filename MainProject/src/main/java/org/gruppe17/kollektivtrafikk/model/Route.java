package org.gruppe17.kollektivtrafikk.model;

import java.util.List;

public class Route {

    private List<Stop> stops;
    private String mode;

    public Route(List<Stop> stops, String mode) {
        this.stops = stops;
        this.mode = mode;
    }

    public List<Stop> getStops() { return stops; }
    public String getMode() { return mode;}

}
