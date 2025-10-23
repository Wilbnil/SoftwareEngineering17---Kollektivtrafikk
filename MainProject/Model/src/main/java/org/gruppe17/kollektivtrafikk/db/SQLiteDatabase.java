package org.gruppe17.kollektivtrafikk.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase {
    public final static String DB_URL = "jdbc:sqlite:MainProject/Model/src/main/java/org/gruppe17/kollektivtrafikk/db/kollektivdatabase.db";
    private Connection connection;

    public SQLiteDatabase() {}

    public Connection startDB() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL);
            return connection;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    public Connection getConnection() {
        return  connection;
    }

    public void stopDB() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
