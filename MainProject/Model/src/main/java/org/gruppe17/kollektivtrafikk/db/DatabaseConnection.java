/*
En klasse for å koble til

 */


package org.gruppe17.kollektivtrafikk.db;


import java.sql.*;
import org.gruppe17.kollektivtrafikk.model.Route;


public class DatabaseConnection {


    // Databaseinformasjon:
    public final static String DB_URL = "jdbc:mysql://localhost:3306/[DatabaseName]";
    public final static String USERNAME = "[Username]";
    public final static String PASSWORD = "[Password]";

/*
    public static Route getRouteFromDatabase(String routeName) {
        // Forbindelse
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD))
        {
            // Erklæring (Statement)
            String statement = "SELECT * FROM routes WHERE name LIKE %" + routeName + "%";
            // Kjør (Execute)
            ResultSet result = statement.executeQuery{

            while (result.next()) {

            }
        }

        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return null;
    }
*/


}
