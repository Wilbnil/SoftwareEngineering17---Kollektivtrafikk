/*
En klasse for å koble til

 */


package org.gruppe17.kollektivtrafikk.db;


import java.sql.*;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;


public class DatabaseConnection {


    // Databaseinformasjon:
    public final static String DB_URL = "jdbc:mysql://localhost:3306/[DatabaseName]";
    public final static String USERNAME = "[Username]";
    public final static String PASSWORD = "[Password]";


    public static Route getRouteFromDatabase(String routeName) {
        // Forbindelse
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD))
        {
            Statement statement = connection.createStatement();
            // Erklæring (Statement)
            String sql = "SELECT * FROM routes WHERE name LIKE '%'" + routeName + "%";
            // Kjør (Execute)
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                int id = result.getInt("id");
                String mode = result.getString("mode");
                java.util.ArrayList<Stop> stops = new java.util.ArrayList<>();

                Route route = new Route(stops, mode);
                route.setId(id);
                return route;
            }


        } catch (SQLException exception) {
            System.err.println("Database error: " + exception.getMessage());
        }
        return null;
    }

}
