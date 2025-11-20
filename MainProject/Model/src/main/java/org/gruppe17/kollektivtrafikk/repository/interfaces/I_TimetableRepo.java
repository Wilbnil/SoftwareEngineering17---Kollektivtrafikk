package org.gruppe17.kollektivtrafikk.repository.interfaces;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;

import java.util.ArrayList;

public interface I_TimetableRepo extends I_Repo<Timetable> {
    int getRouteStopTime(Route route, Stop stop) throws Exception;
    void insertRouteStopTime(Route route, Stop stop, int timeFromStart) throws Exception;
    void updateRouteStopTime(Route route, Stop stop, int newTimeFromStart) throws Exception;
    void deleteRouteStopTime (Route route) throws Exception;

    Timetable getTimetableRouteDay(Route route, String day_of_week) throws Exception;
    ArrayList<Timetable> getTimetablesInRoute(Route route) throws Exception;
    void deleteTimetablesInRoute(Route route) throws Exception;
}
