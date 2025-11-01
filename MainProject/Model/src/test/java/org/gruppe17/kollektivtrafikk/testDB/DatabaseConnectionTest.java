package org.gruppe17.kollektivtrafikk.testDB;

import org.gruppe17.kollektivtrafikk.db.DatabaseAdminSQLAdapter;
import org.gruppe17.kollektivtrafikk.db.DatabaseSQLAdapter;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;

public class DatabaseConnectionTest {
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

    // A test that tests if the Routes are inserted correctly via the insertRoutesIntoDatabase() method
    @Test
    @DisplayName("Route is inserted successfully")
    public void insertRouteIntoDatabase_RouteInsertedSuccessfully() throws Exception {
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates 2 Stop objects and puts them into an ArrayList for use by the Route object
        Stop stop1 = new Stop(7, "testStop1");
        Stop stop2 = new Stop(8, "testStop1");

        // Gets all the stops from Route with id "1"
        ArrayList<Stop> stopsFromRoute1 = sqlAdapter.getAllStopsFromDatabaseInRoute(1);

        // Creates a testStops ArrayList and adds only stop 5 and 6 to the ArrayList
        ArrayList<Stop> testStops = new ArrayList<>();
        testStops.add(stopsFromRoute1.get(4));
        testStops.add(stopsFromRoute1.get(5));

        // Creates a Route object to add to the test-database
        Route testRoute = new Route(3, "Test Route", testStops);

        // Act
        // Runs the insertRouteIntoDatabase() method
        adminSQLAdapter.insertRouteIntoDatabase(testRoute);

        // Uses the getRouteStartEndStops() method to get the start and end stop in a route
        ArrayList<Integer> startEndStops = testDB.getRouteStartEndStops(testRoute.getId());

        // Assert
        // Counts the rows in the "routes" table after one route has been added
        Assertions.assertEquals(3, testDB.countRowsInTable("routes"));
        Assertions.assertEquals("Test Route", testDB.getRouteName(3));
        Assertions.assertTrue(startEndStops.getFirst() == 5 && startEndStops.getLast() == 6);
        Assertions.assertEquals(14 , testDB.countRowsInTable("route_stops"));
    }

    // A test that tests if the Routes are updated correctly via the updateRoutesInDatabase() method
    @Test
    @DisplayName("Route is updated successfully")
    public void updateRouteInDatabase_RouteUpdatedSuccessfully() throws Exception {
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Uses the getAllRoutesFromDatabase() method to get Route 1 for updating
        Route oldRoute = sqlAdapter.getAllRoutesFromDatabase().getFirst();

        // Gets and ArrayList of stops for the Route that will be updated
        ArrayList<Stop> testStops = sqlAdapter.getAllStopsFromDatabaseInRoute(oldRoute.getId());

        // Removes the first and last stop to check if the edited Route will be updated correctly
        testStops.remove(testStops.getFirst());
        testStops.remove(testStops.getLast());

        // Act
        // Updates Route 1 to
        adminSQLAdapter.updateRouteInDatabase(oldRoute, new Route(1, "Test Route", testStops));

        // Uses the getRouteStartEndStops() method to get the start and end stop in a route
        ArrayList<Integer> startEndStops = testDB.getRouteStartEndStops(1);

        // Assert
        Assertions.assertEquals("Test Route", testDB.getRouteName(1));
        Assertions.assertTrue(startEndStops.getFirst() == 2 && startEndStops.getLast() == 5);
    }

    // A test that tests if the Routes are deleted correctly via the deleteRouteInDatabase()
    @Test
    @DisplayName("Route is deleted successfully")
    public void deleteRouteInDatabase_RouteDeletedSuccessfully() throws Exception {
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Uses the getAllRoutesFromDatabase() method to get Route 1 for deleting
        Route oldRoute = sqlAdapter.getAllRoutesFromDatabase().getFirst();

        // Act
        adminSQLAdapter.deleteRouteInDatabase(oldRoute);

        // Assert
        Assertions.assertEquals(1, testDB.countRowsInTable("routes"));
    }

    // A test that tests if the getRoutesFromDatabase method returns the correct data
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
}
