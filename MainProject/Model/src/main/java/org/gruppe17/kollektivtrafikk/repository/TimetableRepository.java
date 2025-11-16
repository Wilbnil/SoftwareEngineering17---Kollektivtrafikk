package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.repository.interfaces.I_TimetableRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.ArrayList;

public class TimetableRepository implements I_TimetableRepo {

    private static Connection connection;

    public TimetableRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Timetable getById(int id) throws Exception {
        String sql =
                "SELECT * FROM timetables " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            result.next();
            int returnId = result.getInt("id");
            int route_id = result.getInt("route_id");
            String day_of_week = result.getString("day_of_week");
            LocalTime first_time = LocalTime.parse(result.getString("first_time"));
            LocalTime last_time = LocalTime.parse(result.getString("last_time"));
            int time_interval = result.getInt("time_interval");
            Timetable returnTimetable = new Timetable(returnId, route_id, day_of_week, first_time, last_time, time_interval);
            return returnTimetable;
    }

    @Override
    public Timetable getTimetableRouteDay(Route route, String day_of_week) throws Exception {
        String sql =
                "SELECT * FROM timetables " +
                        "WHERE route_id = ? AND day_of_week = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, route.getId());
        statement.setString(2, day_of_week);

        ResultSet result = statement.executeQuery();

        result.next();
        int returnId = result.getInt("id");
        int route_id = result.getInt("route_id");
        String returnDay_of_week = result.getString("day_of_week");
        LocalTime first_time = LocalTime.parse(result.getString("first_time"));
        LocalTime last_time = LocalTime.parse(result.getString("last_time"));
        int time_interval = result.getInt("time_interval");
        Timetable returnTimetable = new Timetable(returnId, route_id, returnDay_of_week, first_time, last_time, time_interval);
        return returnTimetable;
    }

    // Unused
    @Override
    public Timetable getByName(String name) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Timetable> getAll() throws Exception {
        String sql =
                "SELECT * FROM timetables;";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        ArrayList<Timetable> returnTimetables = new ArrayList<>();

        while(result.next()) {
            int returnId = result.getInt("id");
            int route_id = result.getInt("route_id");
            String day_of_week = result.getString("day_of_week");
            LocalTime first_time = LocalTime.parse(result.getString("first_time"));
            LocalTime last_time = LocalTime.parse(result.getString("last_time"));
            int time_interval = result.getInt("time_interval");
            returnTimetables.add(new Timetable(returnId, route_id, day_of_week, first_time, last_time, time_interval));
        }

        return returnTimetables;
    }

    @Override
    public ArrayList<Timetable> getTimetablesInRoute(Route route) throws Exception {
        String sql =
                "SELECT * FROM timetables " +
                        "WHERE route_id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, route.getId());

        ResultSet result = statement.executeQuery();

        ArrayList<Timetable> returnTimetables = new ArrayList<>();

        while(result.next()) {
            int returnId = result.getInt("id");
            int route_id = result.getInt("route_id");
            String day_of_week = result.getString("day_of_week");
            LocalTime first_time = LocalTime.parse(result.getString("first_time"));
            LocalTime last_time = LocalTime.parse(result.getString("last_time"));
            int time_interval = result.getInt("time_interval");
            returnTimetables.add(new Timetable(returnId, route_id, day_of_week, first_time, last_time, time_interval));
        }

        return returnTimetables;
    }

    @Override
    public void insert(Timetable object) throws Exception {
        String sql =
                "INSERT INTO timetables (route_id, day_of_week, first_time, last_time, time_interval) " +
                "VALUES (?, ?, ?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, object.getRoute_id());
        statement.setString(2, object.getDay_of_week());
        statement.setString(3, object.getFirst_time().toString());
        statement.setString(4, object.getLast_time().toString());
        statement.setInt(5, object.getTimeInterval());

        int rowsAdded = statement.executeUpdate();
        System.out.println(rowsAdded + " Rows added in timetables");
    }

    @Override
    public void update(Timetable object, Timetable newObject) throws Exception {
        String sql =
                "UPDATE timetables " +
                "SET route_id = ?, day_of_week = ?, first_time = ?, last_time = ?, time_interval = ? " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, newObject.getRoute_id());
            statement.setString(2, newObject.getDay_of_week());
            statement.setString(3, newObject.getFirst_time().toString());
            statement.setString(4, newObject.getLast_time().toString());
            statement.setInt(5, newObject.getTimeInterval());
            // WHERE
            statement.setInt(6, object.getId());

            statement.executeUpdate();
            System.out.println("Timetable " + object.getId() + " has been updated");
    }

    @Override
    public void delete(Timetable object) throws Exception {
        String sql =
                "DELETE FROM timetables " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, object.getId());

            statement.executeUpdate();
        System.out.println("Timetable " + object.getId() + " has been deleted");
    }

    @Override
    public void deleteTimetablesInRoute(Route route) throws Exception {
        String sql =
                "DELETE FROM timetables " +
                        "WHERE route_id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, route.getId());

        int rowsDeleted = statement.executeUpdate();
        System.out.println(rowsDeleted + " rows in timetables have been deleted");
    }

    @Override
    public int getRouteStopTime(Route route, Stop stop) throws Exception {
        String sql =
                "SELECT time_from_start FROM route_stop_time " +
                        "WHERE route_id = ? AND stop_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, route.getId());
        statement.setInt(2, stop.getId());

        ResultSet result = statement.executeQuery();

        result.next();

        int time_from_start = result.getInt("time_from_start");

        return time_from_start;
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
}
