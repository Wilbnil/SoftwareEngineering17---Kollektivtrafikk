package org.gruppe17.kollektivtrafikk.model;

import java.util.ArrayList;


/**
 * The {@code Route} class represents a transportation route between a selection of stops.
 * Transport type is defined which can for example be "bus(buss)" or "train(tog)" using the String.
 * A route will have a unique identifier in the form of a number generated in the database.
 * An {@code ArrayList} will be used to store the stops that the vehicles that drive on the route will stop at.
 * <p>
 * Routes will be used to categorize tours with the same stops.
 * <p>
 * Note that to create a route without an id, a list with Stop objects also have to be made.
 * It is recommended to create the Stops before the Route is created with a list of the Stops belonging to the Route.
 * <p>
 * Only regular get and set methods are provided.
 * {@code RouteService} will handle more logic for routes.
 */


public class Route {

    // Variables
    private int id;
    private String name;
    private ArrayList<Stop> stops;
    private String type;


    // Constructors
    public Route(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Route(String name, ArrayList<Stop> stops, String type) {
        this.name = name;
        this.stops = stops;
        this.type = type;
    }

    public Route(int id, String name, ArrayList<Stop> stops, String type) {
        this.id = id;
        this.name = name;
        this.stops = stops;
        this.type = type;
    }


    // Standard get and set methods.
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Stop> getStops() {
        return new ArrayList<>(stops);
    }

    public String getType() {
        return type;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    //Return the index of the first stop, or -1 if stops is null or empty.
    public Stop getStartStop() {
        return stops.get(0);
    }

    //Return the index of the last stop, or -1 if stops is null or empty.
    public Stop getEndStop() {
        return stops.get(stops.size() - 1);
    }

    public void setId(int id) {
        this.id = id;
    }

}
