package org.gruppe17.kollektivtrafikk.model;

import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The {@code Tour} class represents an individual instance of a route.
 * Contrary to a general {@code Route}, a {@code Tour} takes place on a specific date and time.
 * A {@code Tour} is what the passenger is interested in knowing about, so they can plan their journey easier.
 * The {@code Timetable} class handles the data which need to be stored to create Tours.
 * <p>
 * For handling the time an {@code ArrayList} is used. All the arrival times of the tour should be placed here,
 * and in the same order as the Stops. This has to be done to keep the arrival time and stops correlated.
 *<p>
 *{@code LocalDate} is used for the date variable and {@code LocalTime} is used for the list of arrival times.
 *{@code DistanceCalculator} is also used, but in the method that calculates the duration of the entire Tour.
 * This method might not be relevant for the MVP though,
 * as the user will more generally be interested in the time between their boarded and disembarked stops.
 *<p>
 *
 *
 */


public class Tour {

    private int routeId;
    private ArrayList<LocalTime> arrivals;
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
