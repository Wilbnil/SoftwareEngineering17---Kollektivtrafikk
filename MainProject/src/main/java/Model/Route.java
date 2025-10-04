package Model;

import java.util.List;

//Represents a transport route with a list of stops and a transport mode.
public class Route {

    private List<Stop> stops;
    private String mode;

    public Route(List<Stop> stops, String mode) {
        this.stops = stops;
        this.mode = mode;
    }

    public List<Stop> getStops() { return stops; }
    public String getMode() { return mode;}

}
