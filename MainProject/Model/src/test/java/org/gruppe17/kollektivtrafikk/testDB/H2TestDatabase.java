package org.gruppe17.kollektivtrafikk.testDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2TestDatabase extends TestDatabase {
    public final static String DB_NAME = "testdb";

    public final static String URL = "jdbc:h2:mem:" + DB_NAME;

    public String getURL() {
        return URL;
    }

    public Connection startDB() throws Exception{
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(URL);
        return connection;
    }

    public void stopDB() throws Exception{
        connection.close();
    }
}
