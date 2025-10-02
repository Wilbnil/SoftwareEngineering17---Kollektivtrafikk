package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.Arrays;
import java.util.List;

public class RouteService {

    public Route getRoute(String from, String to) {
        if (from.equals("Fredrikstad") && to.equals("Ostfoldhallen")) {
            List<Stop> stops = Arrays.asList(new Stop("Fredrikstad", 1), new Stop("Ostfoldhallen", 2));
            return new Route(stops,  "bus 17");
        } else if (from.equals("Greaaker") && to.equals("Amfi Borg")) {
            List<Stop> stops = Arrays.asList(new Stop("Greaaker", 3), new Stop("Amfi Borg", 4));
            return new Route(stops,  "bus 7");
        } else if (from.equals("Torsbekke") && to.equals("Fredrikstad")) {
            List<Stop> stops = Arrays.asList(new Stop("Torsbekke", 5), new Stop("Fredrikstad", 1));
            return new Route(stops, "bus 33");
        }
        return null;

    }



}
