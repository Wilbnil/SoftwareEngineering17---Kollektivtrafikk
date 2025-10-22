package org.gruppe17.kollektivtrafikk.testDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public abstract class TestDatabase {

    protected Connection connection;

    public abstract Connection startDB() throws Exception;

    public abstract void stopDB() throws Exception;

    public void createTables() throws Exception{
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE routes(" +
                        "id INTEGER, " +
                        "name TEXT, " +
                        "start_stop INTEGER, " +
                        "end_stop TEXT, " +
                        "PRIMARY KEY(id))");

            statement.execute(
                    "CREATE TABLE stops(" +
                    "id INTEGER, " +
                    "name TEXT NOT NULL, " +
                    "town TEXT, " +
                    "latitude FLOAT, " +
                    "longitude FLOAT, " +
                    "roof INTEGER, " +
                    "accessibility INTEGER, " +
                    "PRIMARY KEY(id))");

            statement.execute(
                    "CREATE TABLE route_stops(" +
                    "route_id INTEGER NOT NULL, " +
                    "stop_id INTEGER NOT NULL, " +
                    "stop_order INTEGER NOT NULL, " +
                    "PRIMARY KEY (route_id, stop_id))");
        }
    }

    public void createDummyData() throws Exception{
        try (Statement statement = connection.createStatement()) {
            insertIntoRoutes(1, "Rute 1", 1, 6);
            insertIntoRoutes(2, "Rute 2", 6, 1);

            insertIntoStops(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139,10.9403,0,0);
            insertIntoStops(2, "Østfoldhallen", "Fredrikstad", 59.2516, 10.9931, 0, 0);
            insertIntoStops(3, "Greåker", "Sarpsborg", 59.2661, 11.0349, 0, 0);
            insertIntoStops(4, "AMFI Borg", "Sarpsborg", 59.2741, 11.0822, 0, 0);
            insertIntoStops(5, "Torsbekken", "Sarpsborg", 59.284, 11.0984, 0, 0);
            insertIntoStops(6, "Sarpsborg bussterminal", "Sarpsborg", 59.283, 11.1071, 0, 0);

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

    public void insertIntoRoutes(int id, String name, int start_stop, int end_stop)
            throws Exception{
        String sql = "INSERT INTO routes (id, name, start_stop, end_stop) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, start_stop);
            preparedStatement.setInt(4, end_stop);
            preparedStatement.executeUpdate();
        }
    }

    public void insertIntoStops(int id, String name, String town, double latitude, double longitude, int roof, int accessibility) throws Exception{
        String sql = "INSERT INTO stops (id, name, town, latitude, longitude, roof, accessibility) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

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

    public void insertIntoRouteStops(int route_id, int stop_id, int stop_order) throws Exception {
        String sql = "INSERT INTO route_stops (route_id, stop_id, stop_order) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, route_id);
            preparedStatement.setInt(2, stop_id);
            preparedStatement.setInt(3, stop_order);
            preparedStatement.executeUpdate();
        }
    }
}
