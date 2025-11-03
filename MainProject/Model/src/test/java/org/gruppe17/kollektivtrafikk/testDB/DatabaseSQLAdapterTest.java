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
        // This method is important since the AUTO_INCREMENT will not reset in between test without it, causing foreign key constraints
        testDB.updateAutoIncrement();
    }

    // Stops the test database
    @AfterAll
    public static void tearDownTestDB() throws Exception {
        testDB.stopDB();
    }

    // Routes
    // Tests if the getRoutesFromDatabaseFromTo() method returns the correct data
    @Test
    @DisplayName("Routes From and To are returned successfully")
    public void getRoutesFromDatabaseFromTo_RoutesReturnedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Sets the test to find routes where stop 5 and 2 exists and stop 5 is before stop 2
        int start_stop = 5;
        int end_stop = 2;

        // Sets "ruter" as an ArrayList of Routes for testing
        ArrayList<Route> returnedRoutes;

        // Act -----------------------------------------------------------------------------

        // Calls the getRoutesFromDatabase with the test inputs as parameters
        returnedRoutes = sqlAdapter.getRoutesFromDatabaseFromTo(start_stop, end_stop);

        // Assert --------------------------------------------------------------------------

        // Checks if the "returnedRoutes" ArrayList contains exactly 1 Route after calling the method
        Assertions.assertEquals(1, returnedRoutes.size());

        // Checks if the name of the Route matches the name of Route 2 which should be "2"
        Assertions.assertEquals("2", returnedRoutes.get(0).getName());

        // Checks that the size of the stops ArrayList is equal to the amount of stops the Route
        // is connected to in the database
        Assertions.assertEquals(6, returnedRoutes.get(0).getStops().size());

        // Checks if the id of the first Stop in the Route matches the id of the Stop which should be 6
        Assertions.assertEquals(6, returnedRoutes.get(0).getStops().get(0).getId());
    }

    // Tests if the getAllRoutesFromDatabase() method returns the correct data
    @Test
    @DisplayName("Routes are returned successfully")
    public void getAllRoutesFromDatabase_RoutesReturnedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an ArrayList of Routes
        ArrayList<Route> returnedRoutes;

        // Act -----------------------------------------------------------------------------

        // Adds all Routes into the returnedRoutes ArrayList
        returnedRoutes = sqlAdapter.getAllRoutesFromDatabase();

        // Assert --------------------------------------------------------------------------

        // Checks if the "returnedRoutes" ArrayList contains 2 Routes after calling the method
        Assertions.assertEquals(2, returnedRoutes.size());

        // Checks if the name of the first Route matches the name of Route 1 which should be "1"
        Assertions.assertEquals("1", returnedRoutes.get(0).getName());

        // Checks that the size of the stops ArrayList is equal to the amount of stops the Route
        // is connected to in the database
        Assertions.assertEquals(6, returnedRoutes.get(1).getStops().size());

        // Checks if the id of the fourth Stop in the first Route matches the id of the Stop which should be 4
        Assertions.assertEquals(4, returnedRoutes.get(0).getStops().get(3).getId());

    }

    // Tests if the getRouteFromDatabaseWhere() method returns the correct data
    @Test
    @DisplayName("Route is returned successfully")
    public void getRouteFromDatabaseWhere_RouteReturnedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates the returnedRoute variable
        Route returnedRoute;

        // Act -----------------------------------------------------------------------------

        // Creates a returnedRoute based on Route 2 from the database
        returnedRoute = sqlAdapter.getRouteFromDatabaseWhere(2);

        // Assert --------------------------------------------------------------------------

        // Checks if the name of the Route matches the name of Route 2 which should be "2"
        Assertions.assertEquals("2", returnedRoute.getName());

        // Checks that the size of the stops ArrayList is equal to the amount of stops the Route
        // is connected to in the database
        Assertions.assertEquals(6, returnedRoute.getStops().size());

        // Checks if the id of the first Stop in the Route matches the id of the Stop which should be 6
        Assertions.assertEquals(6, returnedRoute.getStops().get(0).getId());
    }

    // Stops
    // Tests if the getAllStopsFromDatabaseInRoute() method returns the correct data
    @Test
    @DisplayName("Stops are returned successfully")
    public void getAllStopsFromDatabaseInRoute_StopsReturnedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an ArrayList of Stops
        ArrayList<Stop> returnedStops;

        // Act -----------------------------------------------------------------------------

        // Adds all stops in route 2 to the ArrayList
        returnedStops = sqlAdapter.getAllStopsFromDatabaseInRoute(2);

        // Assert --------------------------------------------------------------------------

        // Checks if the number of Stops added is 6
        Assertions.assertEquals(6, returnedStops.size());

        // Checks if the name of the third Stop in order is "AMFI Borg"
        Assertions.assertEquals("AMFI Borg", returnedStops.get(2).getName());
    }

    // Tests if the getAllStopsFromDatabase() method returns the correct data
    @Test
    @DisplayName("Stops are returned successfully")
    public void getAllStopsFromDatabase_StopsReturnedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an ArrayList of Stops
        ArrayList<Stop> returnedStops;

        // Act -----------------------------------------------------------------------------

        // Adds all stops in the Database to the ArrayList
        returnedStops = sqlAdapter.getAllStopsFromDatabase();

        // Assert --------------------------------------------------------------------------

        // Checks if all the 8 stops were succesfully pulled from the database
        Assertions.assertEquals(8, returnedStops.size());

        // Checks if the name of Stop 7 is "Test Stop 7"
        Assertions.assertEquals("Test Stop 7", returnedStops.get(6).getName());
    }

    // Tests if the getStopFromDatabaseWhere() method returns the correct data
    @Test
    @DisplayName("Routes is returned successfully")
    public void getStopFromDatabaseWhere_StopReturnedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates the returnedStop variable
        Stop returnedStop;

        // Act -----------------------------------------------------------------------------

        // Turns the returnedStop into an object of what Stop 5 is in the database
        returnedStop = sqlAdapter.getStopFromDatabaseWhere(5);

        // Assert --------------------------------------------------------------------------

        // Checks if the name of the returned Stop is equal to "Torsbekken" which is
        // the Stop with id 5
        Assertions.assertEquals("Torsbekken", returnedStop.getName());
    }
}
