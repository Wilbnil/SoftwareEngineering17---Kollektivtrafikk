package org.gruppe17.kollektivtrafikk.repository.interfaces;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

public interface I_RouteRepo extends I_Repo<Route> {

    ArrayList<Route> getAllFromTo(int start_stop, int end_stop) throws Exception;
    ArrayList<Stop> getAllStopsInRoute(int route_id) throws Exception;
    void insertRouteStops(Route route) throws Exception;
    void deleteRouteStops(Route route) throws Exception;
}
