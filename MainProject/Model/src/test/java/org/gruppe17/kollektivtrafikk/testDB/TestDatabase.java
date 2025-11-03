package org.gruppe17.kollektivtrafikk.testDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class TestDatabase {

    protected Connection connection;

    // Abstract method to start a connection, this is further handled by the H2TestDatabase Class
    public abstract Connection startDB() throws Exception;

    // Abstract method to stop a connection, this is further handled by the H2TestDatabase Class
    public abstract void stopDB() throws Exception;

    // Creates the tables in the test database to mock the actual database
    public void createTables() throws Exception{
        try (Statement statement = connection.createStatement()) {

            // Creates table "stops"
            statement.execute(
                    "CREATE TABLE stops(" +
                        "id INTEGER AUTO_INCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "town TEXT, " +
                        "latitude FLOAT, " +
                        "longitude FLOAT, " +
                        "roof INTEGER, " +
                        "accessibility INTEGER, " +
                        "PRIMARY KEY(id));");

            // Creates table "routes"
            statement.execute(
                    "CREATE TABLE routes(" +
                        "id INTEGER AUTO_INCREMENT, " +
                        "name TEXT, " +
                        "start_stop INTEGER, " +
                        "end_stop TEXT, " +
                        "PRIMARY KEY(id), " +
                        "FOREIGN KEY(end_stop) REFERENCES stops(id), " +
                        "FOREIGN KEY(start_stop) REFERENCES stops(id));");

            // Creates table "route_stops"
            statement.execute(
                    "CREATE TABLE route_stops(" +
                        "route_id INTEGER NOT NULL, " +
                        "stop_id INTEGER NOT NULL, " +
                        "stop_order INTEGER NOT NULL, " +
                        "PRIMARY KEY (route_id, stop_id)," +
                        "FOREIGN KEY (route_id) REFERENCES routes(id)," +
                        "FOREIGN KEY (stop_id) REFERENCES stops(id));");
        }
    }

    // Uses the InsertInto methods to add dummy data to the tables
    public void createDummyData() throws Exception{
        try (Statement statement = connection.createStatement()) {

            // Inserts data into the "stops" table
            insertIntoStops(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139,10.9403,0,0);
            insertIntoStops(2, "Østfoldhallen", "Fredrikstad", 59.2516, 10.9931, 0, 0);
            insertIntoStops(3, "Greåker", "Sarpsborg", 59.2661, 11.0349, 0, 0);
            insertIntoStops(4, "AMFI Borg", "Sarpsborg", 59.2741, 11.0822, 0, 0);
            insertIntoStops(5, "Torsbekken", "Sarpsborg", 59.284, 11.0984, 0, 0);
            insertIntoStops(6, "Sarpsborg bussterminal", "Sarpsborg", 59.283, 11.1071, 0, 0);
            insertIntoStops(7, "Test Stop 7", "Test Town", 50, 50, 1, 1);
            insertIntoStops(8, "Test Stop 8", "Test Town", 60, 60, 0, 1);

            // Inserts data into the "routes" table
            insertIntoRoutes(1, "1", 1, 6);
            insertIntoRoutes(2, "2", 6, 1);

            // Inserts data into the "route_stops" table
            insertIntoRouteStops(1, 1, 1);
            insertIntoRouteStops(1, 2, 2);
            insertIntoRouteStops(1, 3, 3);
            insertIntoRouteStops(1, 4, 4);
            insertIntoRouteStops(1, 5, 5);
            insertIntoRouteStops(1, 6, 6);
            insertIntoRouteStops(2, 6, 1);
            insertIntoRouteStops(2, 5, 2);
            insertIntoRouteStops(2, 4, 3);
            insertIntoRouteStops(2, 3, 4);
            insertIntoRouteStops(2, 2, 5);
            insertIntoRouteStops(2, 1, 6);
        }
    }

    // Serves as a blueprint for inserting data into the "routes" table
    public void insertIntoRoutes(int id, String name, int start_stop, int end_stop)
            throws Exception{

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO routes (id, name, start_stop, end_stop) " +
                "VALUES (?, ?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, start_stop);
            preparedStatement.setInt(4, end_stop);
            preparedStatement.executeUpdate();
        }
    }

    // Serves as a blueprint for inserting data into the "stop" table
    public void insertIntoStops(int id, String name, String town, double latitude, double longitude, int roof, int accessibility) throws Exception{

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO stops (id, name, town, latitude, longitude, roof, accessibility) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, town);
            preparedStatement.setDouble(4, latitude);
            preparedStatement.setDouble(5, longitude);
            preparedStatement.setInt(6, roof);
            preparedStatement.setInt(7, accessibility);
            preparedStatement.executeUpdate();
        }
    }

    // Serves as a blueprint for inserting data into the "route_stops" table
    public void insertIntoRouteStops(int route_id, int stop_id, int stop_order) throws Exception {

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO route_stops (route_id, stop_id, stop_order) " +
                "VALUES (?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, route_id);
            preparedStatement.setInt(2, stop_id);
            preparedStatement.setInt(3, stop_order);
            preparedStatement.executeUpdate();
        }
    }

    // Serves as a blueprint to count the rows in a table specified by the parameter
    public int countRowsInTable(String tableName) throws Exception {

        // Creates a SELECT statement that returns the number of rows
        String sql ="SELECT COUNT(*) FROM " + tableName;

        // Creates and runs a statement and returns the number of rows
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    // Returns the name from a specified route in the database
    public String getRouteName(int routeId) throws Exception {
        // Creates a SELECT statement that returns the name from a route with a specified id
        String sql = "SELECT name FROM routes WHERE id = ?";

        // Creates and runs the statement and returns the name
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("name");
        }
    }

    // Returns the start_stop and end_stop of a specific route in the database
    public ArrayList<Integer> getRouteStartEndStops(int routeId) throws Exception {

        // Creates a SELECT statement that returns the start_stop and end_stop from a route with a specified id
        String sql = "SELECT start_stop, end_stop FROM routes WHERE id = ?";

        // Creates and runs the statement and returns an ArrayList that contains the start_stop and end_stop
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> startEndStops = new ArrayList<>();
            resultSet.next();
            startEndStops.add(resultSet.getInt(1));
            startEndStops.add(resultSet.getInt(2));
            return startEndStops;
        }
    }

    // Returns the name from a specified stop in the database
    public String getStopName(int stopId) throws Exception {
        // Creates a SELECT statement that returns the name from a stop with a specified id
        String sql = "SELECT name FROM stops WHERE id = ?";

        // Creates and runs the statement and returns the name
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, stopId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("name");
        }
    }
}
