package gruppe17.kollektivtrafikk.service;

import gruppe17.kollektivtrafikk.db.DatabaseConnection;
import gruppe17.kollektivtrafikk.model.Route;
import gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

//Provides services for retrieving transport routes between stops.

public class RouteService {

    private final String[] stops = {"Fredrikstad", "Ostfoldhallen", "Greaaker", "Amfi Borg", "Torsbekke"};

    public Route getRoute(String from, String to) {
        if (from == null || to == null || from.equals(to)) return null;

        ArrayList<Stop> routeStops = new ArrayList<>();

        routeStops.add(new Stop(1, from, from, 0.0, 0.0, true, true));
        routeStops.add(new Stop(2, to, to, 5.0, 5.0, true, true));

        // an example of the bus nr
        String busName = "bus " + ((from.length() + to.length()) % 50);

        return new Route(routeStops, busName);
    }
}