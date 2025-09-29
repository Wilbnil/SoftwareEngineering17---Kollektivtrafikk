package Service;

import Model.Route;
import Model.Stop;

import java.util.Arrays;
import java.util.List;

public class RouteService {

    public Route getRoute(String from, String to) {
        if (from.equals("Fredrikstad") && to.equals("Ostfoldhallen")) {
            List<Stop> stops = Arrays.asList(new Stop("Fredrikstad", 1), new Stop("Ostfoldhallen", 2));
            return new Route(stops,  "bus 17");
        }
        return null;

    }
}
