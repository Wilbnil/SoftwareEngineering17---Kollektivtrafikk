package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Tour;
import org.gruppe17.kollektivtrafikk.repository.interfaces.TimetableRepository_Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class TimetableRepository implements TimetableRepository_Interface {

    private static Connection connection;

    public TimetableRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int getRouteStopTime(Route route, Stop stop) throws Exception {
        String sql =
                "SELECT time_from_start FROM route_stop_time " +
                "WHERE route_id = ? AND stop_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
    }

    @Override
    public void insertRouteStopTime(Route route, Stop stop, int timeFromStart) throws Exception {
        // When we insert a new Route, we must add it into the route_stops table
        String sql =
                "INSERT INTO route_stop_time (route_id, stop_id, time_from_start) " +
                "VALUES (?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, route.getId());
            statement.setInt(2, stop.getId());
            statement.setInt(3, timeFromStart);

            int rowsAdded = statement.executeUpdate();
            System.out.println(rowsAdded + " Rows added in route_stop_time");
    }

    @Override
    public void updateRouteStopTime(Route route, Stop stop, int newTimeFromStart) throws Exception {
        // Creates a sql-query which updates times in the route_stop_time table
        String sql =
                "UPDATE route_stop_time " +
                "SET time_from_start = ? " +
                "WHERE route_id = ? AND stop_id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newTimeFromStart);
            statement.setInt(2, route.getId());
            statement.setInt(3, stop.getId());

            // Executes the query and prints out the time_from_start that was updated
            statement.executeUpdate();
            System.out.println("time_from_start for " + stop.getName() + " has been updated");
    }

    @Override
    public void deleteRouteStopTime(Route route) throws Exception {
        // When deleting a Route, we must also delete the Route from the route_stops_time table
        String sql =
                "DELETE FROM route_stop_time " +
                "WHERE route_id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Route objects
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, route.getId());

            // Executes the query and prints out the Route that was deleted in route_stop_time
            statement.executeUpdate();
            System.out.println("Route id " + route.getId() + " has been deleted from route_stop_time");
    }

    @Override
    public Tour getById(int id) throws Exception {
        return null;
    }

    @Override
    public Tour getByName(String name) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Tour> getAll() throws Exception {
        return null;
    }

    @Override
    public void insert(Tour object) throws Exception {

    }

    @Override
    public void update(Tour object, Tour newObject) throws Exception {

    }

    @Override
    public void delete(Tour object) throws Exception {

    }
}
