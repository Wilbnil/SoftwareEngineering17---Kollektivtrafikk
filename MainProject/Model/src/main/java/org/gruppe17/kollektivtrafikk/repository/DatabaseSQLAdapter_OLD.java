package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseSQLAdapter_OLD {

private static Connection connection;

    // Accepts a Connection when an SQL Adapter object is created
    public DatabaseSQLAdapter_OLD(Connection connection) {
        this.connection = connection;
    }

    // SELECT queries
    // Routes
    public static ArrayList<Route> getRoutesFromDatabaseFromTo(int startStop, int endStop) {

        // Checks if startStop is the same as the endStop
        // The only reason for this is to prevent users breaking the system
        if (startStop == endStop) {
            return null;
        } else {

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
                try(PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {

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

                    // Runs the getStopsFromDatabase() method to get all the stops for the Route(s)
                    for (Route routeX : routeReturn) {
                        routeX.setStops(getAllStopsFromDatabaseInRoute(routeX.getId()));
                    }

                    //Returns an ArrayList that contains all the Routes that match the input
                    return routeReturn;

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }

            // Returns null if the database is unable to connect
            return null;
        }
    }

    public static ArrayList<Route> getAllRoutesFromDatabase() {

        // SQL-query for returning all Routes
        String routeQuery =
                "SELECT id, name FROM routes " +
                "ORDER BY id; ";

        try(PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {

            // Executes the query
            ResultSet routeResult = routeStatement.executeQuery();

            // Prepares an ArrayList of Routes
            ArrayList<Route> routeReturn = new ArrayList<>();

            // Runs through the resultset and puts the Route objects into the routeReturn ArrayList
            while (routeResult.next()) {
                int id = routeResult.getInt("id");
                String name = routeResult.getString("name");

                routeReturn.add(new Route(id, name));
            }

            // Runs the getStopsFromDatabase() method to get all the stops for the Route(s)
            for (Route routeX : routeReturn) {
                routeX.setStops(getAllStopsFromDatabaseInRoute(routeX.getId()));
            }

            //Returns an ArrayList that contains all the Routes that match the input
            return routeReturn;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public static Route getRouteFromDatabaseWhere(int route_id) {
        // SQL-query for returning a Route from a specified id
        String routeQuery =
                "SELECT id, name FROM routes " +
                "WHERE id = ? " +
                "ORDER BY id; ";

        try(PreparedStatement routeStatement = connection.prepareStatement(routeQuery)) {

            // Sets the ? in the sql-query to the route_id parameter
            routeStatement.setInt(1, route_id);

            // Executes the query
            ResultSet routeResult = routeStatement.executeQuery();

            // Since route ids are distinct, we will always only get one row back from the database
            // Puts the id, name and stops into a new Route object and returns it
            routeResult.next();
            int id = routeResult.getInt("id");
            String name = routeResult.getString("name");
            ArrayList<Stop> stops = getAllStopsFromDatabaseInRoute(id);

            Route returnRoute = new Route(id, name, stops);

            //Returns the Route object
            return returnRoute;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    // Stops
    public static ArrayList<Stop> getAllStopsFromDatabaseInRoute(int route_id) {
        // SQL-query to insert Stops into the Route(s)
        String stopQuery =
                "SELECT s.id, s.name, s.town, s.latitude, s.longitude, s.roof, s.accessibility FROM stops AS s " +
                "INNER JOIN route_stops AS rs ON s.id = rs.stop_id " +
                "WHERE rs.route_id = ? " +
                "ORDER BY rs.route_id, rs.stop_order; ";

        // Turns the stopQuery into a prepared statement for execution
        try(PreparedStatement stopStatement = connection.prepareStatement(stopQuery)) {

            // Sets the ? values in the query to the Route ids in order
            // Also serves to protect the database from SQL-injection by checking that the values are integers
            stopStatement.setInt(1, route_id);

            // Executes the query
            ResultSet stopResult = stopStatement.executeQuery();

            // Prepares an arraylist of stops
            ArrayList<Stop> stops = new ArrayList<>();

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
            return stops;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public static ArrayList<Stop> getAllStopsFromDatabase() {
        // SQL-query for returning all Stops
        String stopQuery =
                "SELECT * FROM Stops " +
                "ORDER BY id; ";

        try(PreparedStatement stopStatement = connection.prepareStatement(stopQuery)) {
            // Executes the query
            ResultSet stopResult = stopStatement.executeQuery();

            // Prepares an ArrayList of Stops to add Stops to and return
            ArrayList<Stop> returnStops = new ArrayList<>();

            // Adds Stops to the ArrayList based on the resultset
            while(stopResult.next()) {
                int id = stopResult.getInt("id");
                String name = stopResult.getString("name");
                String town = stopResult.getString("town");
                float latitude = stopResult.getFloat("latitude");
                float longitude = stopResult.getFloat("longitude");
                // Converts the int values in the database to boolean (true if not 0, false if 0)
                boolean roof = (stopResult.getInt("roof") != 0);
                boolean accessibility = (stopResult.getInt("accessibility") != 0);

                returnStops.add(new Stop(id, name, town, latitude,longitude, roof, accessibility));
            }

            //Returns the ArrayList of Stops
            return returnStops;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public static Stop getStopFromDatabaseWhere(int stop_id) {
        // SQL-query for returning a Stop
        String stopQuery =
                "SELECT * FROM Stops " +
                "WHERE id = ?; ";

        try(PreparedStatement stopStatement = connection.prepareStatement(stopQuery)) {

            // Sets the ? in the sql-query to the stop_id parameter
            stopStatement.setInt(1, stop_id);

            // Executes the query
            ResultSet stopResult = stopStatement.executeQuery();

            // Since stop ids are distinct, we will always only get one row back from the database
            // Puts all the values into a Stop object and returns it
            stopResult.next();
            int id = stopResult.getInt("id");
            String name = stopResult.getString("name");
            String town = stopResult.getString("town");
            float latitude = stopResult.getFloat("latitude");
            float longitude = stopResult.getFloat("longitude");
            // Converts the int values in the database to boolean (true if not 0, false if 0)
            boolean roof = (stopResult.getInt("roof") != 0);
            boolean accessibility = (stopResult.getInt("accessibility") != 0);


            Stop returnStop = new Stop(id, name, town, latitude, longitude, roof, accessibility);

            //Returns the stop Object
            return returnStop;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    // Route Stop Time
    public static int getRouteStopTime(Route route, Stop Stop) {
        String routeStopTimeQuery =
                "SELECT time_from_start FROM route_stop_time " +
                "WHERE route_id = ? AND stop_id = ?";

        try(PreparedStatement routeStopTimeStatement = connection.prepareStatement(routeStopTimeQuery)) {



        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }


}
