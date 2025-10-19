package org.gruppe17.kollektivtrafikk.db;

import java.sql.*;
import java.util.ArrayList;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

public class DatabaseConnection {
    // Database URL
    public final static String DB_URL = "jdbc:sqlite:MainProject/Model/src/main/java/org/gruppe17/kollektivtrafikk/db/kollektivdatabase.db";

    // Uses a from and to stop from the user and creates a Route object for each route that contains these stops in that order.
    public static ArrayList<Route> getRoutesFromDatabase(int startStop, int endStop) {

        // Checks if startStop is the same as the endStop
        // The only reason for this is to prevent users breaking the system
        if (startStop == endStop) {
            return null;
        } else {

            // Connection to the database
            try (Connection connection = DriverManager.getConnection(DB_URL)) {

                // SQL-query for finding info about the Route(s) from the two stops
                // The "" + "" is only there to make the code more readable
                String routeQuery =
                        "SELECT id, name FROM routes AS r " +
                        "INNER JOIN route_stops AS rs1 ON rs1.route_id = r.id " +
                        "INNER JOIN route_stops AS rs2 ON rs2.route_id = r.id " +
                        "WHERE rs2.stop_id = ? " +
                        "AND rs1.stop_id = ? " +
                        "AND rs1.stop_order > rs2.stop_order; ";

                // Turns the query into a prepared statement for execution
                PreparedStatement routeStatement = connection.prepareStatement(routeQuery);

                // Sets the ? values in the query to the input Stop ids in order
                // Also serves to protect the database from SQL-injection by checking that the values are integers
                routeStatement.setInt(1, startStop);
                routeStatement.setInt(2, endStop);

                // Executes the query
                ResultSet routeResult = routeStatement.executeQuery();

                // Prepares an ArrayList of Routes
                ArrayList<Route> routeReturn = new ArrayList<Route>();

                // Runs through the resultset and puts the Route objects into the routeReturn ArrayList
                while (routeResult.next()) {
                    int id = routeResult.getInt("id");
                    String name = routeResult.getString("name");

                    routeReturn.add(new Route(id, name));
                }


                // SQL-query to insert Stops into the Route(s)
                String stopQuery =
                        "SELECT s.id, s.name, s.town, s.latitude, s.longitude, s.roof, s.accessibility FROM stops AS s " +
                        "INNER JOIN route_stops AS rs ON s.id = rs.stop_id " +
                        "WHERE rs.route_id = ? " +
                        "ORDER BY rs.route_id, rs.stop_order; ";

                // Turns the stopQuery into a prepared statement for execution
                PreparedStatement stopStatement = connection.prepareStatement(stopQuery);

                // Puts the Stop query in a for loop for each Route we got from the routeQuery
                for (Route routeX : routeReturn) {

                    // Sets the ? values in the query to the Route ids in order
                    // Also serves to protect the database from SQL-injection by checking that the values are integers
                    stopStatement.setInt(1, routeX.getId());

                    // Executes the query
                    ResultSet stopResult = stopStatement.executeQuery();

                    // Prepares an arraylist of stops
                    ArrayList<Stop> stops = new ArrayList<Stop>();

                    // Runs through the resultset and puts Stop objects into the stops ArrayList
                    while (stopResult.next()) {
                        int id = stopResult.getInt("id");
                        String name = stopResult.getString("name");
                        String town = stopResult.getString("town");
                        float latitude = stopResult.getFloat("latitude");
                        float longitude = stopResult.getFloat("longitude");
                        // Converts the int values in the database to boolean (true if not 0, false if 0)
                        boolean roof = (stopResult.getInt("roof") != 0);
                        boolean accessibility = (stopResult.getInt("accessibility") != 0);

                        stops.add(new Stop(id, name, town, latitude, longitude, roof, accessibility));
                    }

                    // Sets the stops ArrayList in the Route objects to the stops ArrayList we got from the query
                    routeX.setStops(stops);
                }

                //Returns an ArrayList that contains all the Routes that match the input
                return routeReturn;

                // Catches exception if the database is unable to connect
            } catch (SQLException exception) {
                System.err.println(exception.getMessage());
            }
            // Returns null if the database is unable to connect
            return null;
        }
    }



}
