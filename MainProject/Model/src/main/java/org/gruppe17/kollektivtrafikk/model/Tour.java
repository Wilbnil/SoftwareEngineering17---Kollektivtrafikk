package org.kollektivtrafikk.model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Tour {

    private int routeId;
    private ArrayList<LocalTime> arrivals;
    private String mode;

    public LocalTime getDuration() {
        if (arrivals == null || arrivals.size() == 2) return null;
        return arrivals.get(arrivals.size() - 1).minusHours(arrivals.get(0).getHour())
                .minusMinutes(arrivals.get(0).getMinute());
    }

    public LocalTime getStartTime() {
        return arrivals != null && !arrivals.isEmpty() ? arrivals.get(0) : null;
    }

}
