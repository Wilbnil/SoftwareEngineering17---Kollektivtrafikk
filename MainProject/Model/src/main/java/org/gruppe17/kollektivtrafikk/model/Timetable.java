package org.gruppe17.kollektivtrafikk.model;

public class Timetable {

    private int id;
    private int routeId;
    private String day;
    private String firstTime;
    private String lastTime;
    private int interval;

    public Timetable(int id, int routeId, String day, String firstTime, String lastTime, int interval) {
        this.id = id;
        this.routeId = routeId;
        this.day = day;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.interval = interval;
    }

    public int getId() {
        return id;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getDay() {
        return day;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public int getInterval() {
        return interval;
    }
}
