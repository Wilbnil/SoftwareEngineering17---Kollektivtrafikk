package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

//Provides services for retrieving transport routes between stops.

public class RouteService {

    private ArrayList<Stop> stops = new ArrayList<>();

    //Retrieves a route between two stops based on their names.
    public Route getRoute(String from, String to) {
        if (from.equals("Fredrikstad") && to.equals("Ostfoldhallen")) {
            stops.add(new Stop(1, "Fredrikstad"));
            stops.add(new Stop(2, "Ostfoldhallen"));
            return new Route(1, "3", stops,  "bus 17");
        } else if (from.equals("Greaaker") && to.equals("Amfi Borg")) {
            stops.add(new Stop(3, "Greaaker"));
            stops.add(new Stop(4, "Amfi Borg"));
            return new Route(2, "4", stops,  "bus 7");
        } else if (from.equals("Torsbekke") && to.equals("Fredrikstad")) {
            stops.add(new Stop(5, "Torsbekke"));
            stops.add(new Stop(6, "Fredrikstad"));
            return new Route(3, "5", stops, "bus 33");
        }
        return null; //Returns null if no route is found.

    }



}