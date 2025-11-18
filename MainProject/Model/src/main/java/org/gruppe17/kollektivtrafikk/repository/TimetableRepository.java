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

    /**
     * Get Timetable by id
     *
     * @param {int} id - Timetable id
     * @return {Timetable} - Timetable object
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Get Timetable by Route and Day
     *
     * @param {Route} route - Route object
     * @param {String} day_of_week - Day
     * @return {Timetable} - Timetable object
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Get all Timetables
     *
     * @return {ArrayList<Timetable>} - ArrayList of Timetable objects
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Get all Timetables in a Route
     *
     * @param {Route} route - Route object
     * @return {ArrayList<Timetable>} - ArrayList of Timetable objects
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Insert a Timetable
     *
     * @param {Timetable} timetable - Timetable object
     * @throws {Exception} If database connection fails
     */
    @Override
    public void insert(Timetable timetable) throws Exception {
        String sql =
                "INSERT INTO timetables (route_id, day_of_week, first_time, last_time, time_interval) " +
                "VALUES (?, ?, ?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, timetable.getRoute_id());
        statement.setString(2, timetable.getDay_of_week());
        statement.setString(3, timetable.getFirst_time().toString());
        statement.setString(4, timetable.getLast_time().toString());
        statement.setInt(5, timetable.getTimeInterval());

        int rowsAdded = statement.executeUpdate();
        System.out.println(rowsAdded + " Rows added in timetables");
    }

    /**
     * Update a Timetable
     *
     * @param {Timetable} timetable - Timetable for updating
     * @param {Timetable} newTimetable - Updated timetable
     * @throws {Exception} If database connection fails
     */
    @Override
    public void update(Timetable timetable, Timetable newTimetable) throws Exception {
        String sql =
                "UPDATE timetables " +
                "SET route_id = ?, day_of_week = ?, first_time = ?, last_time = ?, time_interval = ? " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, newTimetable.getRoute_id());
            statement.setString(2, newTimetable.getDay_of_week());
            statement.setString(3, newTimetable.getFirst_time().toString());
            statement.setString(4, newTimetable.getLast_time().toString());
            statement.setInt(5, newTimetable.getTimeInterval());
            // WHERE
            statement.setInt(6, timetable.getId());

            statement.executeUpdate();
            System.out.println("Timetable " + timetable.getId() + " has been updated");
    }

    /**
     * Delete a Timetable
     *
     * @param {Timetable} timetable - Timetable for deletion
     * @throws {Exception} If database connection fails
     */
    @Override
    public void delete(Timetable timetable) throws Exception {
        String sql =
                "DELETE FROM timetables " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, timetable.getId());

            statement.executeUpdate();
        System.out.println("Timetable " + timetable.getId() + " has been deleted");
    }

    /**
     * Deletes all Timetables in a Route
     *
     * @param {Route} route - Route object
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Get time_from_start from Route and Stop
     *
     * @param {Route} route - Route object
     * @param {Stop} stop - Stop object
     * @return {int} - time_from_start
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Insert time_from_start into database
     *
     * @param {Route} route - Route object
     * @param {Stop} stop - Stop object
     * @param {int} timeFromStart - Time from start to insert
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Update time_from_start in database
     *
     * @param {Route} route - Route object
     * @param {Stop} stop - Stop object
     * @param {int} newtimeFromStart - Time from start to update with
     * @throws {Exception} If database connection fails
     */
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

    /**
     * Deletes time_from_start in a Route
     *
     * @param {Route} route - Route object
     * @throws {Exception} If database connection fails
     */
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

    // Unused but implements from Interface
    @Override
    public Timetable getByName(String name) throws Exception {
        return null;
    }
}
