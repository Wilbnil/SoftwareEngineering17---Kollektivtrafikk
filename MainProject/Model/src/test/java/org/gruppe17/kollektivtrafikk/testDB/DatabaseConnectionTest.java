package org.gruppe17.kollektivtrafikk.testDB;

import org.gruppe17.kollektivtrafikk.db.DatabaseSQLAdapter;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;

public class DatabaseConnectionTest {
    private final static TestDatabase testDB = new H2TestDatabase();

    private static Connection connection;

    @BeforeAll
    public static void setUpTestDB() throws Exception{
        // Starter test-databasen
        connection = testDB.startDB();
        testDB.createTables();

        // Tillater å senere kunne rulle tilbake data-endringer.
        connection.setAutoCommit(false);
    }

    @BeforeEach
    public void prepareTest() throws Exception{
        testDB.createDummyData();
    }

    @AfterEach
    public void cleanUpTest() throws Exception{
        connection.rollback();
    }

    @AfterAll
    public static void tearDownTestDB() throws Exception {
        testDB.stopDB();
    }

    @Test
    @DisplayName("Routes is returned successfully")
    public void getRoutesFromDatabase_RoutesReturnedSuccessfully() throws Exception {
    // Arrange
    DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

    int start_stop = 5;
    int end_stop = 2;

    ArrayList<Route> ruter;

    // Act
    ruter = sqlAdapter.getRoutesFromDatabase(start_stop, end_stop);

    // Assert
    Assertions.assertEquals(1, ruter.size());
    }
}
