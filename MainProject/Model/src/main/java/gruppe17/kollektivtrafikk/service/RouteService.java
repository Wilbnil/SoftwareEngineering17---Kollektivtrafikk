package gruppe17.kollektivtrafikk.service;

import gruppe17.kollektivtrafikk.db.DatabaseConnection;
import gruppe17.kollektivtrafikk.model.Route;
import gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

//Provides services for retrieving transport routes between stops.

public class RouteService {

    //Retrieves a route between two stops based on their names.
    public Route getRoute(String from, String to) {
        ArrayList<gruppe17.kollektivtrafikk.model.Stop> stops = new ArrayList<>();

        String routeName = from + " to " + to;
        Route route = DatabaseConnection.getRouteFromDatabase(routeName);
        if (route != null) {
            return route;
        }
        if ("Fredrikstad".equals(from) && "Ostfoldhallen".equals(to)) {
            stops.add(new Stop(1, "Fredrikstad", "Fredrikstad", 0.0, 0.0, true, true));
            stops.add(new Stop(2, "Ostfoldhallen", "Fredrikstad", 5.0, 0.0, true, true));
            return new Route(stops,  "bus 17");
        } else if ("Greaaker".equals(from) && "Amfi Borg".equals(to)) {
            stops.add(new Stop(3, "Greaaker", "Amfi Borg", 0.0, 3.0, true, true));
            stops.add(new Stop(4, "Amfi Borg", "Sarpsborg", 3.0, 3.0, true, true));
            return new Route(stops,  "bus 7");
        } else if ("Torsbekke".equals(from) && "Fredrikstad".equals(to)) {
            stops.add(new Stop(5, "Torsbekke", "Sarpsborg", 10.0, 0.0, true, true));
            stops.add(new Stop(6, "Fredrikstad", "Fredrikstad", 0.0, 0.0, true, true));
            return new Route(stops, "bus 33");
        }
        return null; //Returns null if no route is found.

    }
}