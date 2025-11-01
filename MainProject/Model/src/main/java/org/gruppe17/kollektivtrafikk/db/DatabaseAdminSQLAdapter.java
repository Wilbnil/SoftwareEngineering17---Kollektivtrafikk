package org.gruppe17.kollektivtrafikk.db;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseAdminSQLAdapter {

    private static Connection connection;

    public DatabaseAdminSQLAdapter(Connection connection) {
        this.connection = connection;
    }

    // CRUD operations
    // Routes
    public static void insertRouteIntoDatabase(Route route) {

        // Creates a sql-query which inserts values into the "routes" table
        String insertQuery =
                "INSERT INTO routes (id, name, start_stop, end_stop) " +
                "VALUES (?, ?, ?, ?); ";

        // Creates at statement based on the query and inserts the values based on the parameter Route object
        try(PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, route.getId());
            insertStatement.setString(2, route.getName());
            insertStatement.setInt(3, route.getStartStop().getId());
            insertStatement.setInt(4, route.getEndStop().getId());

            // Executes the query and prints out the number of rows added
            int rowsAdded = insertStatement.executeUpdate();
            System.out.println(rowsAdded + " Rows added in routes");

            // Inserts the neccessary data into the route_stops table
            // This has to be done last, as it will trigger foreing key constraints
            insertRouteStopsIntoDatabase(route);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }

    public static void updateRouteInDatabase(Route route, Route newRoute) {
        // Creates a sql-query which updates Routes in the "routes" table
        String updateQuery =
                "UPDATE routes " +
                "SET id = ?, name = ?, start_stop = ?, end_stop = ? " +
                "WHERE id = ?; ";

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        try(PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, newRoute.getId());
            updateStatement.setString(2, newRoute.getName());
            updateStatement.setInt(3, newRoute.getStartStop().getId());
            updateStatement.setInt(4, newRoute.getEndStop().getId());
            updateStatement.setInt(5, route.getId());

            // Creates two Integer ArrayLists that consists of the ids of the route and the newRoute
            // This is to allow us to compare the two ArrayLists, since comparing objects will not work
            ArrayList<Integer> compare_1 = getStopIdsFromArrayListStop(newRoute.getStops());
            ArrayList<Integer> compare_2 = getStopIdsFromArrayListStop(route.getStops());

            // If the ArrayLists are not equal:
            // Delete all rows in the route_stops table for the old route
            // Insert new rows in the route_stops table for the updated route
            if(!(compare_1.equals(compare_2))) {
                deleteRouteStopsInDatabase(route);
                insertRouteStopsIntoDatabase(newRoute);
            }

            // Executes the query and prints out the Route that was updated
            updateStatement.executeUpdate();
            System.out.println("Route " + route.getName() + " has been updated");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void deleteRouteInDatabase(Route route) {
        // Creates a sql-query which updates Routes in the "routes" table
        String deleteQuery =
                "DELETE FROM routes " +
                "WHERE id = ?; ";

        // Deletes all the rows in the route_stops table connected to the deleted route
        // This has to be done first, as it will trigger a foreign key constraint
        deleteRouteStopsInDatabase(route);

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        try(PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, route.getId());

            // Executes the query and prints out the Route that was deleted
            deleteStatement.executeUpdate();
            System.out.println("Route " + route.getName() + " has been deleted");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Stops

    // Route_Stops
    // These are not used as standalone methods, but are used by the insert-, update- and delete Route methods
    private static void insertRouteStopsIntoDatabase(Route route) {
        // When we insert a new Route, we must add it into the route_stops table
        String insertRouteStopsQuery =
                "INSERT INTO route_stops (route_id, stop_id, stop_order) " +
                "VALUES (?, ?, ?); ";

        try(PreparedStatement insertRouteStopsStatement = connection.prepareStatement(insertRouteStopsQuery)) {

            insertRouteStopsStatement.setInt(1, route.getId());

            int size = route.getStops().size();

            for (int i = 0; i < size; i++) {
                insertRouteStopsStatement.setInt(2, route.getStops().get(i).getId());
                insertRouteStopsStatement.setInt(3, i+1);

                int rowsAdded = insertRouteStopsStatement.executeUpdate();
                System.out.println(rowsAdded + " Rows added in route_stops");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void deleteRouteStopsInDatabase(Route route) {
        // When deleting a Route, we must also delete the Route from the route_stops table
        String deleteRouteStopsQuery =
                "DELETE FROM route_stops " +
                "WHERE route_id = ?; ";

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        try(PreparedStatement deleteRouteStopsStatement = connection.prepareStatement(deleteRouteStopsQuery)) {
            deleteRouteStopsStatement.setInt(1, route.getId());

            // Executes the query and prints out the Route that was deleted in route_stops
            deleteRouteStopsStatement.executeUpdate();
            System.out.println("Route id " + route.getId() + " has been deleted from route_stops");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static ArrayList<Integer> getStopIdsFromArrayListStop(ArrayList<Stop> stops) {
        ArrayList<Integer> returnList = new ArrayList<>();

        for(Stop stopX : stops) {
            returnList.add(stopX.getId());
        }

        return returnList;
    }

}
