package org.gruppe17.kollektivtrafikk.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase {
    public final static String DB_URL = "jdbc:sqlite:MainProject/Model/src/main/java/org/gruppe17/kollektivtrafikk/db/kollektivdatabase.db";
    private Connection connection;

    public SQLiteDatabase() {}

    // Starts the Database by creating a Connection that other classes can use
    public void startDB() throws SQLException {
        try {
            // Creates the connection from the URL
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    // Allows other classes to access the connection which is created
    public Connection getConnection() {
        return  connection;
    }

    // Closes the connection to the database
    public void stopDB() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
