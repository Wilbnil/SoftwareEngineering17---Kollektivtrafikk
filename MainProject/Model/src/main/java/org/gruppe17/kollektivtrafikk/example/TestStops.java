package org.kollektivtrafikk.example;

import gruppe17.kollektivtrafikk.db.DatabaseConnection;
import gruppe17.kollektivtrafikk.model.Stop;

//Test class to demonstrate the functionality of transport stop and route management.

public class TestStops {
    public static void main(String[] args) {
        DatabaseConnection dbc = new DatabaseConnection();

        Stop test = dbc.getRouteFromDatabase(1);

        System.out.println(test.getId());
        System.out.println(test.getName());
        System.out.println(test.getTown());
        System.out.println(test.getLongitude());
        System.out.println(test.getLatitude());


    }
}

