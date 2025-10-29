package org.gruppe17.kollektivtrafikk.db;

import org.gruppe17.kollektivtrafikk.model.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAdminSQLAdapter {

    private static Connection connection;

    public DatabaseAdminSQLAdapter(Connection connection) {
        this.connection = connection;
    }

    // Method for inserting Routes into the "routes" table
    public void insertRouteIntoDatabase(Route route) {

        // Creates a sql-query which inserts values into the "routes" table
        String insertQuery =
                "INSERT INTO routes (id, name, start_stop, end_stop) " +
                "VALUES (?, ?, ?, ?)";

        // Creates at statement based on the query and inserts the values based on the parameter Route object
        try(PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, route.getId());
            insertStatement.setString(2, route.getName());
            insertStatement.setInt(3, route.getStartStop().getId());
            insertStatement.setInt(4, route.getEndStop().getId());

            // Executes the query and prints out the number of rows added
            int rowsAdded = insertStatement.executeUpdate();
            System.out.println(rowsAdded + " Rows added");

        } catch (SQLException e) {
            System.out.println("e");
        }
    }
}
