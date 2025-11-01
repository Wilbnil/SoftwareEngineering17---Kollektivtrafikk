package org.gruppe17.kollektivtrafikk.testDB;

import org.gruppe17.kollektivtrafikk.db.DatabaseSQLAdapter;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;

public class DatabaseSQLAdapterTest {
    private final static TestDatabase testDB = new H2TestDatabase();

    private static Connection connection;

    // Creates a connection and calls for the creation of all the tables in the test database
    @BeforeAll
    public static void setUpTestDB() throws Exception{
        // Starts the test database
        connection = testDB.startDB();

        // Creates the tables
        testDB.createTables();

        // Allows the test database to roll back data
        connection.setAutoCommit(false);
    }

    // Inserts the dummy data for testing
    @BeforeEach
    public void prepareTest() throws Exception{
        testDB.createDummyData();
    }

    // Rolls back the data after the test as to not hinder other tests from working correctly
    @AfterEach
    public void cleanUpTest() throws Exception{
        connection.rollback();
    }

    // Stops the test database
    @AfterAll
    public static void tearDownTestDB() throws Exception {
        testDB.stopDB();
    }

    // Routes
    // Tests if the getRoutesFromDatabase method returns the correct data
    @Test
    @DisplayName("Routes is returned successfully")
    public void getRoutesFromDatabase_RoutesReturnedSuccessfully() throws Exception {
    // Arrange
    // Creates the SQL Adapters connection to test the method
    DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

    // Sets the test to find routes where stop 5 and 2 exists and stop 5 is before stop 2
    int start_stop = 5;
    int end_stop = 2;

    // Sets "ruter" as an ArrayList of Routes for testing
    ArrayList<Route> route;

    // Creates an ArrayList of Stops and adds all the stops that should be retrieved from the getRoutesFromDatabase() method
    ArrayList<Stop> stops = new ArrayList<>();
    stops.add(new Stop(6, "Sarpsborg bussterminal", "Sarpsborg", 59.283, 11.1071, false, false));
    stops.add(new Stop(5, "Torsbekken", "Sarpsborg", 59.284, 11.0984, false, false));
    stops.add(new Stop(4, "AMFI Borg", "Sarpsborg", 59.2741, 11.0822, false, false));
    stops.add(new Stop(3, "Greåker", "Sarpsborg", 59.2661, 11.0349, false, false));
    stops.add(new Stop(2, "Østfoldhallen", "Fredrikstad", 59.2516, 10.9931, false, false));
    stops.add(new Stop(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139,10.9403,false,false));

    // Creates an ArrayList of Routes and adds the route that should be retrieved from the getRoutesFromDatabase() method
    ArrayList<Route> test_route = new ArrayList<>();
    test_route.add(new Route(2, "2", stops));

    // Act
    // Calls the getRoutesFromDatabase with the test inputs as parameters
    route = sqlAdapter.getRoutesFromDatabaseFromTo(start_stop, end_stop);

    // Assert
    // Checks if the "ruter" ArrayList contains exactly 1 Route after calling the method
    Assertions.assertEquals(1, route.size());

    // Checks if the id of the Route returned by the getRoutesFromDatabase() method matches what it's supposed to be
    Assertions.assertTrue(test_route.get(0).getId() == route.get(0).getId());

    // Checks if the name of the Route returned by the getRoutesFromDatabase() method matches what it's supposed to be
    Assertions.assertTrue(test_route.get(0).getName() == route.get(0).getName());

    // Checks if the id of the first Stop in the Route returned by the getRoutesFromDatabase() method matches what it's supposed to be
    Assertions.assertTrue(test_route.get(0).getStops().get(0).getId() == route.get(0).getStops().get(0).getId());
    }

    // Stops
    //
}
