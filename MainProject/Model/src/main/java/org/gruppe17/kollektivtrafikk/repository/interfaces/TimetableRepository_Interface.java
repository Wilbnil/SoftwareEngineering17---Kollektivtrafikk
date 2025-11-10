package org.gruppe17.kollektivtrafikk.repository.interfaces;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Tour;

import java.sql.SQLException;

public interface TimetableRepository_Interface extends Repository_Interface<Tour> {
    int getRouteStopTime(Route route, Stop stop) throws Exception;
    void insertRouteStopTime(Route route, Stop stop, int timeFromStart) throws Exception;
    void updateRouteStopTime(Route route, Stop stop, int newTimeFromStart) throws Exception;
    void deleteRouteStopTime (Route route) throws Exception;

}
