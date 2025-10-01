package org.gruppe17.kollektivtrafikk.databaseConnections;
import org.gruppe17.kollektivtrafikk.databaseConnections.model.Route;

import java.net.NoRouteToHostException;
import java.sql.*;

public class DatabaseMain {


    // Databaseinformasjon:
    public final static String DB_URL = "jdbc:mysql://localhost:3306/[DatabaseName]";
    public final static String USERNAME = "[Username]";
    public final static String PASSWORD = "[Password]";

    public static void main(String[] args) {

        // ?
    }

    public static Route getRouteFromDatabase(String userSearch) {
        // Forbindelse
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD))
        {
            // Erklæring (Statement)
            String statement = "SELECT * FROM routes WHERE name LIKE %" + userSearch + "%";
            // Kjør (Execute)
            // Route route = ResultSet result...

        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return route;
    }



}
