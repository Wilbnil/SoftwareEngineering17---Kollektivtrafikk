package org.gruppe17.kollektivtrafikk.model;

import java.time.LocalTime;


/**
 * The {@code Timetable} class represents the data that needs to be stored to create instances of the {@code Tour} class.
 * This class will be used to create Tours for the user to view.
 * <p>
 *  </p>
 * The same variables as the columns in the timetable-table in the public transport database (kollektivtadatabase)
 * are used in this class. Both the route id and an additional unique identifier for the timetable itself is used,
 * represented by integers. A day is also stored to allow different timetables of the same route to apply to different
 * days of the week.
 * <p>
 * The first and last times of a {@code Route} on the given day is stored with the {@code LocalTime} class.
 * This is to create specific Tours with a time. These times represent when a {@code Tour} starts. Together with the
 * time interval integer, all the Tours for a day can be created from a single row in the timetable-table.
 * The time interval represents the time between each {@code Tour} starting.
 */

public class Timetable {
    // Variables
    private int id;
    private int route_id;
    private String day_of_week;
    private LocalTime first_time;
    private LocalTime last_time;
    private int time_interval;


    // Constructors
    public Timetable(int id, int route_id, String day_of_week, LocalTime first_time, LocalTime last_time, int time_interval) {
        this.id = id;
        this.route_id = route_id;
        this.day_of_week = day_of_week;
        this.first_time = first_time;
        this.last_time = last_time;
        this.time_interval = time_interval;
    }

    public Timetable(int route_id, String day_of_week, LocalTime first_time, LocalTime last_time, int time_interval) {
        this.route_id = route_id;
        this.day_of_week = day_of_week;
        this.first_time = first_time;
        this.last_time = last_time;
        this.time_interval = time_interval;
    }


    // Standard get and set methods.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
    }

    public LocalTime getFirst_time() {
        return first_time;
    }

    public void setFirst_time(LocalTime first_time) {
        this.first_time = first_time;
    }

    public LocalTime getLast_time() {
        return last_time;
    }

    public void setLast_time(LocalTime last_time) {
        this.last_time = last_time;
    }

    public int getTimeInterval() {
        return time_interval;
    }

    public void setInterval(int time_interval) {
        this.time_interval = time_interval;
    }
}
