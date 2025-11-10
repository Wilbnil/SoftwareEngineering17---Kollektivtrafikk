package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.interfaces.I_RouteRepo;
import org.gruppe17.kollektivtrafikk.utility.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RepositoryRoute implements I_RouteRepo {

    private static Connection connection;

    public RepositoryRoute(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Route getById(int id) throws Exception {
        // SQL-query for returning a Route from a specified id
        String sql =
                "SELECT id, name, type FROM routes " +
                "WHERE id = ? " +
                "ORDER BY id; ";

        PreparedStatement statement = connection.prepareStatement(sql);

        // Sets the ? in the sql-query to the route_id parameter
        statement.setInt(1, id);

        // Executes the query
        ResultSet result = statement.executeQuery();

        // Since route ids are distinct, we will always only get one row back from the database
        // Puts the id, name and stops into a new Route object and returns it
        result.next();
        int routeId = result.getInt("id");
        String routeName = result.getString("name");
        ArrayList<Stop> stops = getAllStopsInRoute(id);
        String type = result.getString("type");

        Route returnRoute = new Route(routeId, routeName, stops, type);

        //Returns the Route object
        return returnRoute;
    }

    @Override
    public Route getByName(String name) throws Exception {
        // SQL-query for returning a Route from a specified id
        String routeQuery =
                "SELECT id, name, type FROM routes " +
                "WHERE name = ? " +
                "ORDER BY id; ";

        PreparedStatement routeStatement = connection.prepareStatement(routeQuery);

            // Sets the ? in the sql-query to the route_id parameter
            routeStatement.setString(1, name);

            // Executes the query
            ResultSet result = routeStatement.executeQuery();

            // Since route ids are distinct, we will always only get one row back from the database
            // Puts the id, name and stops into a new Route object and returns it
            result.next();
            int id = result.getInt("id");
            String routeName = result.getString("name");
            ArrayList<Stop> stops = getAllStopsInRoute(id);
            String type = result.getString("type");

            Route returnRoute = new Route(id, routeName, stops, type);

            //Returns the Route object
            return returnRoute;
    }

    @Override
    public ArrayList<Route> getAll() throws Exception {
        // SQL-query for returning all Routes
        String sql =
                "SELECT id, name, type FROM routes " +
                "ORDER BY id; ";

        PreparedStatement statement = connection.prepareStatement(sql);

            // Executes the query
            ResultSet result = statement.executeQuery();

            // Prepares an ArrayList of Routes
            ArrayList<Route> routeReturn = new ArrayList<>();

            // Runs through the resultset and puts the Route objects into the routeReturn ArrayList
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String type = result.getString("type");

                routeReturn.add(new Route(id, name, type));
            }

            // Runs the getAllInRoute() method to get all the stops for the Route(s)
            for (Route routeX : routeReturn) {
                routeX.setStops(getAllStopsInRoute(routeX.getId()));
            }

            //Returns an ArrayList that contains all the Routes that match the input
            return routeReturn;
    }

    @Override
    public ArrayList<Route> getAllFromTo(int start_stop, int end_stop) throws Exception {
        // Checks if startStop is the same as the endStop
        // The only reason for this is to prevent users breaking the system
        if (start_stop == end_stop) {
            return null;
        } else {

            // SQL-query for finding info about the Route(s) from the two stops
            // The "" + "" is only there to make the code more readable
            String sql =
                    "SELECT id, name, type FROM routes AS r " +
                    "INNER JOIN route_stops AS rs1 ON rs1.route_id = r.id " +
                    "INNER JOIN route_stops AS rs2 ON rs2.route_id = r.id " +
                    "WHERE rs2.stop_id = ? " +
                    "AND rs1.stop_id = ? " +
                    "AND rs1.stop_order > rs2.stop_order; ";

            // Turns the query into a prepared statement for execution
            PreparedStatement statement = connection.prepareStatement(sql);

            // Sets the ? values in the query to the input Stop ids in order
            // Also serves to protect the database from SQL-injection by checking that the values are integers
            statement.setInt(1, start_stop);
            statement.setInt(2, end_stop);

            // Executes the query
            ResultSet result = statement.executeQuery();

            // Prepares an ArrayList of Routes
            ArrayList<Route> routeReturn = new ArrayList<>();

            // Runs through the resultset and puts the Route objects into the routeReturn ArrayList
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String type = result.getString("type");

                routeReturn.add(new Route(id, name, type));
            }

            // Runs the getStopsFromDatabase() method to get all the stops for the Route(s)
            for (Route routeX : routeReturn) {
                routeX.setStops(getAllStopsInRoute(routeX.getId()));
            }

            //Returns an ArrayList that contains all the Routes that match the input
            return routeReturn;
        }
    }

    @Override
    public ArrayList<Stop> getAllStopsInRoute(int route_id) throws Exception {
        // SQL-query to insert Stops into the Route(s)
        String sql =
                "SELECT s.id, s.name, s.town, s.latitude, s.longitude, s.roof, s.accessibility FROM stops AS s " +
                "INNER JOIN route_stops AS rs ON s.id = rs.stop_id " +
                "WHERE rs.route_id = ? " +
                "ORDER BY rs.route_id, rs.stop_order; ";

        // Turns the stopQuery into a prepared statement for execution
        PreparedStatement statement = connection.prepareStatement(sql);

        // Sets the ? values in the query to the Route ids in order
        // Also serves to protect the database from SQL-injection by checking that the values are integers
        statement.setInt(1, route_id);

        // Executes the query
        ResultSet result = statement.executeQuery();

        // Prepares an arraylist of stops
        ArrayList<Stop> stops = new ArrayList<>();

        // Runs through the resultset and puts Stop objects into the stops ArrayList
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            String town = result.getString("town");
            float latitude = result.getFloat("latitude");
                float longitude = result.getFloat("longitude");
            // Converts the int values in the database to boolean (true if not 0, false if 0)
            boolean roof = (result.getInt("roof") != 0);
            boolean accessibility = (result.getInt("accessibility") != 0);

            stops.add(new Stop(id, name, town, latitude, longitude, roof, accessibility));
        }

        // Sets the stops ArrayList in the Route objects to the stops ArrayList we got from the query
        return stops;
    }

    @Override
    public void insert(Route route) throws Exception {
        // Creates a sql-query which inserts values into the "routes" table
        String sql =
                "INSERT INTO routes (id, name, start_stop, end_stop, type) " +
                "VALUES (?, ?, ?, ?, ?);";

        // Creates at statement based on the query and inserts the values based on the parameter Route object
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, route.getId());
            statement.setString(2, route.getName());
            statement.setInt(3, route.getStartStop().getId());
            statement.setInt(4, route.getEndStop().getId());
            statement.setString(5, route.getType());

            // Executes the query and prints out the number of rows added
            int rowsAdded = statement.executeUpdate();
            System.out.println(rowsAdded + " Rows added in routes");

            // Inserts the neccessary data into the route_stops table
            // This has to be done last, as it will trigger foreing key constraints
            insertRouteStops(route);
    }

    @Override
    public void update(Route object, Route newObject) throws Exception {
        // Creates a sql-query which updates Routes in the "routes" table
        // Id can not be updated as it is an Auto Increment id
        String sql =
                "UPDATE routes " +
                "SET name = ?, start_stop = ?, end_stop = ?, SET type = ? " +
                "WHERE id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newObject.getName());
            statement.setInt(2, newObject.getStartStop().getId());
            statement.setInt(3, newObject.getEndStop().getId());
            statement.setString(4, newObject.getType());
            // WHERE
            statement.setInt(5, object.getId());

            // Creates two Integer ArrayLists that consists of the ids of the route and the newRoute
            // This is to allow us to compare the two ArrayLists, since comparing objects will not work
            DatabaseUtility dbUtil = new DatabaseUtility();
            ArrayList<Integer> compare_1 = dbUtil.getStopIdsFromStops(newObject.getStops());
            ArrayList<Integer> compare_2 = dbUtil.getStopIdsFromStops(object.getStops());

            // If the ArrayLists are not equal:
            // Delete all rows in the route_stops table for the old route
            // Insert new rows in the route_stops table for the updated route
            if(!(compare_1.equals(compare_2))) {
                deleteRouteStops(object);
                insertRouteStops(newObject);
            }

            // Executes the query and prints out the Route that was updated
            statement.executeUpdate();
            System.out.println("Route " + object.getName() + " has been updated");
    }

    @Override
    public void delete(Route object) throws Exception {
        // Creates a sql-query which updates Routes in the "routes" table
        String sql =
                "DELETE FROM routes " +
                "WHERE id = ?;";

        // Deletes all the rows in the route_stops, route_stop_time and timetables tables connected to the deleted route
        // This has to be done first, as it will trigger a foreign key constraint
        deleteRouteStops(object);
        deleteRouteStopTime(object);

        // Creates at statement based on the query and inserts the values based on the parameter Route object
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, object.getId());

            // Executes the query and prints out the Route that was deleted
            statement.executeUpdate();
            System.out.println("Route " + object.getName() + " has been deleted");
    }

    @Override
    public void insertRouteStops(Route route) throws Exception {
        // When we insert a new Route, we must add it into the route_stops table
        String sql =
                "INSERT INTO route_stops (route_id, stop_id, stop_order) " +
                "VALUES (?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, route.getId());

            int size = route.getStops().size();

            for (int i = 0; i < size; i++) {
                statement.setInt(2, route.getStops().get(i).getId());
                statement.setInt(3, i + 1);

                int rowsAdded = statement.executeUpdate();
                System.out.println(rowsAdded + " Rows added in route_stops");
            }
    }

    @Override
    public void deleteRouteStops(Route route) throws Exception {
        // When deleting a Route, we must also delete the Route from the route_stops table
        String sql =
                "DELETE FROM route_stops " +
                "WHERE route_id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, route.getId());

            // Executes the query and prints out the Route that was deleted in route_stops
            statement.executeUpdate();
            System.out.println("Route id " + route.getId() + " has been deleted from route_stops");
    }
}
