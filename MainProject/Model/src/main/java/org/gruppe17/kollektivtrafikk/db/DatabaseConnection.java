package org.kollektivtrafikk.db;

import java.io.File;
import java.sql.*;

import gruppe17.kollektivtrafikk.model.Stop;

public class DatabaseConnection {
    // Databaseinformasjon:
    public final static String DB_URL = "jdbc:sqlite:kollektivdatabase.db";

    public static Stop getRouteFromDatabase(int start) {

        System.out.println(new File(".").getAbsolutePath());

        // Forbindelse
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM stops WHERE id = ?";

            PreparedStatement stmnt = connection.prepareStatement(query);

            stmnt.setInt(1, start);

            ResultSet result = stmnt.executeQuery();

            while (result.next()) {
                Stop stop = new Stop(result.getInt("id"), result.getString("name"), result.getString("town"), result.getFloat("longitude"), result.getFloat("latitude"), result.getBoolean("roof"), result.getBoolean("accessibility"));
                return stop;
            }


        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return null;
    }



}
