package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.interfaces.I_StopRepo;
import org.gruppe17.kollektivtrafikk.utility.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StopRepository implements I_StopRepo {

    private static Connection connection;

    public StopRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stop getById(int id) throws Exception {
        // SQL-query for returning a Stop
        String sql =
                "SELECT * FROM Stops " +
                "WHERE id = ?; ";

        PreparedStatement statement = connection.prepareStatement(sql);

            // Sets the ? in the sql-query to the stop_id parameter
            statement.setInt(1, id);

            // Executes the query
            ResultSet result = statement.executeQuery();

            // Since stop ids are distinct, we will always only get one row back from the database
            // Puts all the values into a Stop object and returns it
            result.next();
            int stopId = result.getInt("id");
            String name = result.getString("name");
            String town = result.getString("town");
            float latitude = result.getFloat("latitude");
            float longitude = result.getFloat("longitude");
            // Converts the int values in the database to boolean (true if not 0, false if 0)
            boolean roof = (result.getInt("roof") != 0);
            boolean accessibility = (result.getInt("accessibility") != 0);

            Stop returnStop = new Stop(stopId, name, town, latitude, longitude, roof, accessibility);

            //Returns the stop Object
            return returnStop;
    }

    @Override
    public Stop getByName(String name) throws Exception {
        // SQL-query for returning a Stop
        String sql =
                "SELECT * FROM Stops " +
                "WHERE name = ?; ";

        PreparedStatement statement = connection.prepareStatement(sql);

        // Sets the ? in the sql-query to the stop_id parameter
        statement.setString(1, name);

        // Executes the query
        ResultSet result = statement.executeQuery();

        // Since stop ids are distinct, we will always only get one row back from the database
        // Puts all the values into a Stop object and returns it
        result.next();
        int id = result.getInt("id");
        String stopName = result.getString("name");
        String town = result.getString("town");
        float latitude = result.getFloat("latitude");
        float longitude = result.getFloat("longitude");
        // Converts the int values in the database to boolean (true if not 0, false if 0)
        boolean roof = (result.getInt("roof") != 0);
        boolean accessibility = (result.getInt("accessibility") != 0);

        Stop returnStop = new Stop(id, stopName, town, latitude, longitude, roof, accessibility);

        //Returns the stop Object
        return returnStop;
    }

    @Override
    public ArrayList<Stop> getAll() throws Exception {
        // SQL-query for returning all Stops
        String sql =
                "SELECT * FROM Stops " +
                "ORDER BY id; ";

        PreparedStatement statement = connection.prepareStatement(sql);

            // Executes the query
            ResultSet result = statement.executeQuery();

            // Prepares an ArrayList of Stops to add Stops to and return
            ArrayList<Stop> returnStops = new ArrayList<>();

            // Adds Stops to the ArrayList based on the resultset
            while(result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String town = result.getString("town");
                float latitude = result.getFloat("latitude");
                float longitude = result.getFloat("longitude");
                // Converts the int values in the database to boolean (true if not 0, false if 0)
                boolean roof = (result.getInt("roof") != 0);
                boolean accessibility = (result.getInt("accessibility") != 0);

                returnStops.add(new Stop(id, name, town, latitude,longitude, roof, accessibility));
            }

            //Returns the ArrayList of Stops
            return returnStops;
    }

    @Override
    public void insert(Stop object) throws Exception {
        // Creates a sql-query which inserts values into the "stops" table
        String sql =
                "INSERT INTO stops (name, town, latitude, longitude, roof, accessibility) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        // Creates at statement based on the query and inserts the values based on the parameter Stop object
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, object.getName());
            statement.setString(2, object.getTown());
            statement.setFloat(3, (float) object.getLatitude());
            statement.setFloat(4, (float) object.getLongitude());
            // Converts the boolean into an int
            DatabaseUtility dbUtil = new DatabaseUtility();
            int roofInt = dbUtil.returnIntFromBool(object.getRoof());
            int accessibilityInt = dbUtil.returnIntFromBool(object.getAccessibility());
            statement.setInt(5, roofInt);
            statement.setInt(6, accessibilityInt);

            // Executes the query and prints out the number of rows added
            int rowsAdded = statement.executeUpdate();
            System.out.println(rowsAdded + " Rows added in stops");
    }

    @Override
    public void update(Stop object, Stop newObject) throws Exception {
        // Creates a sql-query which updates Stops in the "stops" table
        // Id can not be updated as it is an Auto Increment id
        String sql =
                "UPDATE stops " +
                "SET name = ?, town = ?, latitude = ?, longitude = ?, roof = ?, accessibility = ? " +
                "WHERE id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Stop objects
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newObject.getName());
            statement.setString(2, newObject.getTown());
            statement.setFloat(3, (float) newObject.getLatitude());
            statement.setFloat(4, (float) newObject.getLongitude());
            // Converts the boolean into an int
            DatabaseUtility dbUtil = new DatabaseUtility();
            int roofInt = dbUtil.returnIntFromBool(newObject.getRoof());
            int accessibilityInt = dbUtil.returnIntFromBool(newObject.getAccessibility());
            statement.setInt(5, roofInt);
            statement.setInt(6, accessibilityInt);
            // WHERE
            statement.setInt(7, object.getId());

            // Executes the query and prints out the Stop that was updated
            statement.executeUpdate();
            System.out.println("Stop " + object.getName() + " has been updated");
    }

    @Override
    public void delete(Stop object) throws Exception {
        // Creates a sql-query which updates Stops in the "stops" table
        // Note that no special measure has to be taken if the Admin tries to delete a stop that is in use by a Route
        // This will not work since it will cause a foreign key constraint
        String sql =
                "DELETE FROM stops " +
                "WHERE id = ?;";

        // Creates at statement based on the query and inserts the values based on the parameter Stop object
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, object.getId());

            // Executes the query and prints out the Stop that was deleted
            statement.executeUpdate();
            System.out.println("Stop " + object.getName() + " has been deleted");
    }
}
