package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.interfaces.I_RouteRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The {@code RouteRepository} handles all SQL-Queries to the database regarding the "routes"
 * table and the "route_stops" table
 * <p>
 * The only exception is the getAllStopsInRoute() method, as it is used by all get methods
 * to be able to return the stops ArrayList for the Route object
 * </p>
 * <p>
 * The Connection entails a database connection and can for example be the connection
 * from the SQLiteDatabase class' startDB() method
 * </p>
 * Example usages:
 * <blockquote><pre>
 *     RouteRepository routeRepo = new RouteRepository(connection);
 *     Route returnedRoute = routeRepo.getById(2);
 * </pre></blockquote>
 * <p>
 *     Using an Interface to instantiate RouteRepository can be beneficial to allow multiple
 *     different classes that implements the Repository Interface to be instantiated depending on which Repository you want to use
 * </p>
 * <blockquote><pre>
 *     I_RouteRepo routeRepo = new RouteRepository(connection);
 *     Route returnedRoute = routeRepo.getById(2);
 * </pre></blockquote>
 */

public class RouteRepository implements I_RouteRepo {

    private static Connection connection;

    public RouteRepository(Connection connection) {
        this.connection = connection;
    }


    /**
     * Get Route by id
     *
     * @param {int} id - Route id
     * @return {Route} - Route object
     * @throws {Exception} If database connection fails
     */
    @Override
    public Route getById(int id) throws Exception {
        // SQL-query for returning a Route from a specified id
        String sql =
                "SELECT id, name, type FROM routes " +
                "WHERE id = ? " +
                "ORDER BY id; ";

        // Turns the query into a prepared statement for execution
        PreparedStatement statement = connection.prepareStatement(sql);

        // Sets the ? in the sql-query to the id parameter
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

    /**
     * Get Route by name
     *
     * @param {String} name - Route name
     * @return {Route} - Route object
     * @throws {Exception} If database connection fails
     */
    @Override
    public Route getByName(String name) throws Exception {
        // SQL-query for returning a Route from a specified name
        String routeQuery =
                "SELECT id, name, type FROM routes " +
                "WHERE name = ? " +
                "ORDER BY id; ";

        // Turns the query into a prepared statement for execution
        PreparedStatement routeStatement = connection.prepareStatement(routeQuery);

            // Sets the ? in the sql-query to the name parameter
            routeStatement.setString(1, name);

            // Executes the query
            ResultSet result = routeStatement.executeQuery();

            // Since route names are distinct, we will always only get one row back from the database
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

    /**
     * Get all Routes in database
     *
     * @return {ArrayList<Route>} - ArrayList of Route objects
     * @throws {Exception} If database connection fails
     */
    @Override
    public ArrayList<Route> getAll() throws Exception {
        // SQL-query for returning all Routes
        String sql =
                "SELECT id, name, type FROM routes " +
                "ORDER BY id; ";

        // Turns the query into a prepared statement for execution
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

    /**
     * Get all Routes that contains a Stop from and to
     *
     * @param {int} start_stop - Stop id from
     * @param {int} end_stop - Stop id to
     * @return @return {ArrayList<Route>} - ArrayList of Route objects
     * @throws {Exception} If start_stop and end_stop is equal
     * @throws {Exception} If database connection fails
     */
    @Override
    public ArrayList<Route> getAllFromTo(int start_stop, int end_stop) throws Exception {
        // Checks if startStop is the same as the endStop
        // This prevents the method from returning null
        if (start_stop == end_stop) {
            throw new Exception("Start_stop and end_stop cannot be equal");
        } else {

            // SQL-query for finding info about the Route(s) from the two stops
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

    /**
     * Get all Stops in a Route
     *
     * @param {int} route_id - Route id
     * @return {ArrayList<Stop>} - ArrayList of Stop objects
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Insert a Route into the database
     *
     * @param {Route} route - Route to insert
     * @throws {Exception} If database connection fails
     *
     * Must use insertRouteStops with the same route after the insert method is called to
     * prevent a faulty database. This keeps the code loosely connected
     * Example usage:
     * <pre>
     *     routeRepo.insert(route);
     *     routeRepo.insertRouteStops(route);
     * </pre>
     */
    @Override
    public void insert(Route route) throws Exception {
        // Creates a sql-query which inserts values into the "routes" table
        String sql =
                "INSERT INTO routes (name, start_stop, end_stop, type) " +
                "VALUES (?, ?, ?, ?);";

        // Creates at statement based on the query and inserts the values based on the parameter Route object
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, route.getName());
            statement.setInt(2, route.getStartStop().getId());
            statement.setInt(3, route.getEndStop().getId());
            statement.setString(4, route.getType());

            // Executes the query and prints out the number of rows added
            int rowsAdded = statement.executeUpdate();
            System.out.println(rowsAdded + " Rows added in routes");
    }

    /**
     * Update a Route in the database
     *
     * @param {Route} route - Route to update
     * @param {Route} newRoute - Updated route
     * @throws {Exception} If database connection fails
     *
     * Must use deleteRouteStops, deleteRouteStopTime and deleteTimetable before the update
     * method is called and use insertRouteStops after the update method is called to prevent a faulty database.
     * This keeps the code loosely connected
     * Example usage:
     * <pre>
     *     timetableRepo.deleteTimetable(route);
     *     timetableRepo.deleteRouteStopTime(route);
     *     routeRepo.deleteRouteStops(route);
     *     routeRepo.update(route, newRoute);
     *     routeRepo.insertRouteStops(newRoute);
     * </pre>
     */
    @Override
    public void update(Route route, Route newRoute) throws Exception {
        // Creates a sql-query which updates Routes in the "routes" table
        // Id can not be updated as it is an Auto Increment id
        String sql =
                "UPDATE routes " +
                "SET name = ?, start_stop = ?, end_stop = ?, type = ? " +
                "WHERE id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newRoute.getName());
            statement.setInt(2, newRoute.getStartStop().getId());
            statement.setInt(3, newRoute.getEndStop().getId());
            statement.setString(4, newRoute.getType());
            // WHERE
            statement.setInt(5, route.getId());

            // Executes the query and prints out the Route that was updated
            statement.executeUpdate();
            System.out.println("Route " + route.getName() + " has been updated");
    }

    /**
     * Delete a Route in the database
     *
     * @param {Route} route - Route to delete
     * @throws {Exception} If database connection fails
     *
     * Use deleteRouteStops, deleteRouteStopTime and deleteTimetable before the delete method
     * is called to prevent a faulty database. This keeps the code loosely connected
     * Example usage:
     * <pre>
     *     timetableRepo.deleteTimetable(route);
     *     timetableRepo.deleteRouteStopTime(route);
     *     routeRepo.deleteRouteStops(route);
     *     routeRepo.delete(route);
     * </pre>
     */
    @Override
    public void delete(Route route) throws Exception {
        // Creates a sql-query which updates Routes in the "routes" table
        String sql =
                "DELETE FROM routes " +
                "WHERE id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Route object
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, route.getId());

            // Executes the query and prints out the Route that was deleted
            statement.executeUpdate();
            System.out.println("Route " + route.getName() + " has been deleted");
    }

    /**
     * Insert Stops into a Route via the route_stops table
     *
     * @param {Route} route - Route for inserting
     * @throws {Exception} If database connection fails
     */
    // Don't need their own tests as they are automatically being tested in the insert, update and delete
    // tests. They are also methods which will only be used alongside the aformentioned methods
    @Override
    public void insertRouteStops(Route route) throws Exception {
        // When we insert or updating a new Route, we must add it into the route_stops table
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

    /**
     * Delete Stops in a Route via the route_stops table
     *
     * @param {Route} route - Route for deleting
     * @throws {Exception} If database connection fails
     */
    @Override
    public void deleteRouteStops(Route route) throws Exception {
        // When deleting or updating a Route, we must also delete the Route from the route_stops table
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
