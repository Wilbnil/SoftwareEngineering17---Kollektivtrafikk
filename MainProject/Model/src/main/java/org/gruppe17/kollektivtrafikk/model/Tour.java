package org.gruppe17.kollektivtrafikk.model;

import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;

import java.time.LocalTime;
import java.util.ArrayList;

public class Tour {

    private int routeId;
    private ArrayList<LocalTime> arrivals;
    private String mode;


    public LocalTime getDuration() {
        if (arrivals == null || arrivals.size() == 2) return null;
        return DistanceCalculator.calculateTravelTime(arrivals.getFirst(), arrivals.getLast());
    }

    public LocalTime getStartTime() {
        return arrivals != null && !arrivals.isEmpty() ? arrivals.get(0) : null;
    }

}
