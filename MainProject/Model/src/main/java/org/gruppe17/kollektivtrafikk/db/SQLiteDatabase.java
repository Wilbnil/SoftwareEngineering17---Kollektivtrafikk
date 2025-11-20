package org.gruppe17.kollektivtrafikk.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase {
    private Connection connection;
    private String DB_URL;

    public SQLiteDatabase(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    // Starts the Database by creating a Connection that other classes can use
    public Connection startDB() throws SQLException {
            // Creates the connection from the URL
            connection = DriverManager.getConnection(DB_URL);
            return connection;
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
