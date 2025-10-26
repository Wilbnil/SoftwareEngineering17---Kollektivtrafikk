package org.gruppe17.kollektivtrafikk.testDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2TestDatabase extends TestDatabase {

    // Creates a database with the name "testdb"
    public final static String DB_NAME = "testdb";

    // Uses H2 to create a database URL to a database that sits in the memory instead of a server/file
    public final static String URL = "jdbc:h2:mem:" + DB_NAME;

    // Allows other classes to access the URL
    public String getURL() {
        return URL;
    }

    // Handles the abstract startDB() method and returns a connection which can be used by the tests
    public Connection startDB() throws Exception{
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(URL);
        return connection;
    }

    // Handles the abstract stopDB() method and returns a connection which can be used by the tests
    public void stopDB() throws Exception{
        connection.close();
    }
}
