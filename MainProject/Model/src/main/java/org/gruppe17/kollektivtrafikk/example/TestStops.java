package org.gruppe17.kollektivtrafikk.example;

import org.gruppe17.kollektivtrafikk.db.DatabaseConnection;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

//Test class to demonstrate the functionality of transport stop and route management.

public class TestStops {
    public static void main(String[] args) {
        DatabaseConnection dbc = new DatabaseConnection();

        ArrayList<Route> test = dbc.getRoutesFromDatabase(2, 5);

        for (Route routeX : test) {
            System.out.println("Route ---------------");
            System.out.println("Route ID: " + routeX.getId());
            System.out.println("Route Name: " + routeX.getName());
            System.out.println("");

            for (Stop stopX : routeX.getStops()) {
                System.out.println("Stop ---------------");
                System.out.println("Stop ID: " + stopX.getId());
                System.out.println("Stop Name: " + stopX.getName());
                System.out.println("Stop Town: " + stopX.getTown());
                System.out.println("Stop Latitude: " + stopX.getLatitude());
                System.out.println("Stop Longitude: " + stopX.getLongitude());
                System.out.println("Stop Rood: " + stopX.getRoof());
                System.out.println("Stop Accessibility: " + stopX.getAccessibility());
                System.out.println("");
            }
        }
    }
}

