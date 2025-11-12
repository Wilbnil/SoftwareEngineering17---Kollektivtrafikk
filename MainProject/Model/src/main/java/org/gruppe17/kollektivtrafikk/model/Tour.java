package org.gruppe17.kollektivtrafikk.model;

import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Tour {

    private int routeId;
    private ArrayList<LocalTime> arrivals;
    private String type;
    private LocalDate date;

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public ArrayList<LocalTime> getArrivals() {
        return new ArrayList<>(arrivals);
    }

    public String getType() {
        return type;
    }

    public LocalTime getDuration() {
        if (arrivals == null || arrivals.size() == 2) return null;
        return DistanceCalculator.calculateTravelTime(arrivals.getFirst(), arrivals.getLast());
    }

    public LocalTime getStartTime() {
        return arrivals != null && !arrivals.isEmpty() ? arrivals.get(0) : null;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
