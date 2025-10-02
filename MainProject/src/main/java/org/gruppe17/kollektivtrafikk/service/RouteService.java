package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

public class RouteService {

    private ArrayList<Stop> stops = new ArrayList<>();

    public Route getRoute(String from, String to) {
        if (from.equals("Fredrikstad") && to.equals("Ostfoldhallen")) {
            stops.add(new Stop("Fredrikstad", 1));
            stops.add(new Stop("Ostfoldhallen", 2));
            return new Route(stops,  "bus 17");
        } else if (from.equals("Greaaker") && to.equals("Amfi Borg")) {
            stops.add(new Stop("Greaaker", 3));
            stops.add(new Stop("Amfi Borg", 4));
            return new Route(stops,  "bus 7");
        } else if (from.equals("Torsbekke") && to.equals("Fredrikstad")) {
            stops.add(new Stop("Torsbekke", 5));
            stops.add(new Stop("Fredrikstad", 1));
            return new Route(stops, "bus 33");
        }
        return null;

    }



}
