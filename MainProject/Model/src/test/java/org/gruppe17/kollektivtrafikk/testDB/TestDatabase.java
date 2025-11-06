package org.gruppe17.kollektivtrafikk.testDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.h2.util.ParserUtil.FOREIGN;

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
                        "PRIMARY KEY(route_id, stop_id), " +
                        "FOREIGN KEY(route_id) REFERENCES routes(id), " +
                        "FOREIGN KEY(stop_id) REFERENCES stops(id));");

            // Creates table "timetables"
            statement.execute(
                    "CREATE TABLE timetables (" +
                        "id INTEGER AUTO_INCREMENT, " +
                        "route_id INTEGER NOT NULL, " +
                        "day_of_week TEXT NOT NULL, " +
                        "first_time TEXT NOT NULL, " +
                        "last_time TEXT NOT NULL, " +
                        "time_interval INTEGER NOT NULL, " +
                        "PRIMARY KEY(id), " +
                        "FOREIGN KEY(route_id) REFERENCES routes(id));");

            // Creates table "route_stop_time"
            statement.execute(
                    "CREATE TABLE route_stop_time (" +
                        "route_id INTEGER, " +
                        "stop_id INTEGER, " +
                        "time_from_start INTEGER, " +
                        "PRIMARY KEY(route_id, stop_id), " +
                        "FOREIGN KEY(route_id,stop_id) REFERENCES route_stops(route_id,stop_id));");
        }
    }

    // Uses the InsertInto methods to add dummy data to the tables
    public void createDummyData() throws Exception{
        try (Statement statement = connection.createStatement()) {
            // Inserts data into the "stops" table
            insertIntoStops("Fredrikstad bussterminal", "Fredrikstad", 59.2139,10.9403,0,0);
            insertIntoStops("Østfoldhallen", "Fredrikstad", 59.2516, 10.9931, 0, 0);
            insertIntoStops("Greåker", "Sarpsborg", 59.2661, 11.0349, 0, 0);
            insertIntoStops("AMFI Borg", "Sarpsborg", 59.2741, 11.0822, 0, 0);
            insertIntoStops("Torsbekken", "Sarpsborg", 59.284, 11.0984, 0, 0);
            insertIntoStops("Sarpsborg bussterminal", "Sarpsborg", 59.283, 11.1071, 0, 0);
            insertIntoStops("Test Stop 7", "Test Town", 50, 50, 1, 1);
            insertIntoStops("Test Stop 8", "Test Town", 60, 60, 0, 1);

            // Inserts data into the "routes" table
            insertIntoRoutes("1", 1, 6);
            insertIntoRoutes("2", 6, 1);

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

            // Inserts data into the "timetables" table
            insertIntoTimetables(1, "monday", "05:30", "00:25", 10);
            insertIntoTimetables(1, "tuesday", "05:30", "00:25", 10);
            insertIntoTimetables(1, "wednesday", "05:30", "00:25", 10);
            insertIntoTimetables(1, "thursday", "05:30", "00:25", 10);
            insertIntoTimetables(1, "friday", "05:30", "00:25", 10);
            insertIntoTimetables(1, "saturday", "07:45", "00:25", 15);
            insertIntoTimetables(1, "sunday", "09:30", "23:55", 30);
            insertIntoTimetables(2, "monday", "05:30", "00:25", 10);
            insertIntoTimetables(2, "tuesday", "05:30", "00:25", 10);
            insertIntoTimetables( 2, "wednesday", "05:30", "00:25", 10);
            insertIntoTimetables( 2, "thursday", "05:30", "00:25", 10);
            insertIntoTimetables( 2, "friday", "05:30", "00:25", 10);
            insertIntoTimetables( 2, "saturday", "07:45", "00:25", 15);
            insertIntoTimetables( 2, "sunday", "09:30", "23:55", 30);

            // Inserts data into the "route_stop_time" table
            insertIntoRouteStopTime(1, 1, 0);
            insertIntoRouteStopTime(1, 2, 8);
            insertIntoRouteStopTime(1, 3, 13);
            insertIntoRouteStopTime(1, 4, 19);
            insertIntoRouteStopTime(1, 5, 24);
            insertIntoRouteStopTime(1, 6, 25);
            insertIntoRouteStopTime(2, 6, 0);
            insertIntoRouteStopTime(2, 5, 8);
            insertIntoRouteStopTime(2, 4, 13);
            insertIntoRouteStopTime(2, 3, 19);
            insertIntoRouteStopTime(2, 2, 24);
            insertIntoRouteStopTime(2, 1, 25);
        }
    }

    // Updates the AUTO_INCREMENTs to go back to starting at 1
    public void updateAutoIncrement() throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE stops ALTER COLUMN id RESTART WITH 1;");
            statement.execute("ALTER TABLE routes ALTER COLUMN id RESTART WITH 1;");
            statement.execute("ALTER TABLE timetables ALTER COLUMN id RESTART WITH 1;");
        }
    }

    // Serves as a blueprint for inserting data into the "routes" table
    public void insertIntoRoutes(String name, int start_stop, int end_stop)
            throws Exception{

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO routes (name, start_stop, end_stop) " +
                "VALUES (?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //preparedStatement.setInt(1, id);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, start_stop);
            preparedStatement.setInt(3, end_stop);
            preparedStatement.executeUpdate();
        }
    }

    // Serves as a blueprint for inserting data into the "stop" table
    public void insertIntoStops(String name, String town, double latitude, double longitude, int roof, int accessibility) throws Exception{

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO stops (name, town, latitude, longitude, roof, accessibility) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //preparedStatement.setInt(1, id);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, town);
            preparedStatement.setDouble(3, latitude);
            preparedStatement.setDouble(4, longitude);
            preparedStatement.setInt(5, roof);
            preparedStatement.setInt(6, accessibility);
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

    public void insertIntoTimetables(int route_id, String day_of_week, String first_time, String last_time, int time_interval) throws Exception {

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO timetables (route_id, day_of_week, first_time, last_time, time_interval) " +
                "VALUES (?, ?, ?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, route_id);
            preparedStatement.setString(2, day_of_week);
            preparedStatement.setString(3, first_time);
            preparedStatement.setString(4, last_time);
            preparedStatement.setInt(5, time_interval);
        }
    }

    public void insertIntoRouteStopTime(int route_id, int stop_id, int time_from_start) throws Exception {

        // Creates an INSERT INTO statement for use
        String sql = "INSERT INTO route_stop_time (route_id, stop_id, time_from_start) " +
                "VALUES (?, ?, ?)";

        // Places the input parameter values of the method into the statement above
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, route_id);
            preparedStatement.setInt(2, stop_id);
            preparedStatement.setInt(3, time_from_start);
        }
    }

    // Test Methods
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
