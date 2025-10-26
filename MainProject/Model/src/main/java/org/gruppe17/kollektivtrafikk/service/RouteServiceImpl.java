package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;


public class RouteServiceImpl implements RouteService {

    private final String[] stops = {"Fredrikstad", "Ostfoldhallen", "Greaaker", "Amfi Borg", "Torsbekke"};

    @Override
    public Route getRoute(String from, String to) {
        if (from == null || to == null || from.equals(to)) return null;

        ArrayList<Stop> routeStops = new ArrayList<>();
        routeStops.add(new Stop(1, from, from, 0.0, 0.0, true, true));
        routeStops.add(new Stop(2, to, to, 5.0, 5.0, true, true));

        String busName = "bus " + ((from.length() + to.length()) % 50);

        return new Route(1, from + " - " + to, routeStops, busName);
    }
}
