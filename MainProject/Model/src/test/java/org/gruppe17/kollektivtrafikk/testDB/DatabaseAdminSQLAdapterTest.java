package org.gruppe17.kollektivtrafikk.testDB;

import org.gruppe17.kollektivtrafikk.db.DatabaseAdminSQLAdapter;
import org.gruppe17.kollektivtrafikk.db.DatabaseSQLAdapter;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseAdminSQLTest {
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
    // Tests if the Routes are inserted correctly via the insertRoutesIntoDatabase() method
    @Test
    @DisplayName("Route is inserted successfully")
    public void insertRouteIntoDatabase_RouteInsertedSuccessfully() throws Exception {
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

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

    // Tests if the Routes are updated correctly via the updateRoutesInDatabase() method
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
        Assertions.assertEquals(10, testDB.countRowsInTable("route_stops"));
    }

    // Tests if the Routes are deleted correctly via the deleteRouteInDatabase()
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
        Assertions.assertEquals(6, testDB.countRowsInTable("route_stops"));
    }

    // Stops
    // Tests if the Stops are inserted correctly via the insertStopsIntoDatabase() method
    @Test
    @DisplayName("Stop is inserted successfully")
    public void insertStopIntoDatabase_StopInsertedSuccessfully() throws Exception{
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);

        // Creates a new stop to add into the database
        Stop testStop = new Stop(9, "Test Stop 9", "Test Town", 50, 50, true, false);

        // Act
        adminSQLAdapter.insertStopIntoDatabase(testStop);

        // Assert
        Assertions.assertEquals(9, testDB.countRowsInTable("stops"));
        Assertions.assertEquals("Test Stop 9", testDB.getStopName(testStop.getId()));
    }

    // Tests if the Stops are updated correctly via the updateStopsInDatabase() method
    @Test
    @DisplayName("Stop is updated successfully")
    public void updateStopInDatabase_StopUpdatedSuccessfully() throws Exception{
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        Stop stop_6 = sqlAdapter.getStopFromDatabaseWhere(6);

        Stop newStop_6 = new Stop(6, "Test Stop 6", "Test Town", 60, 60, true, true);

        // Act
        adminSQLAdapter.updateStopInDatabase(stop_6, newStop_6);

        // Assert
        Assertions.assertEquals("Test Stop 6", testDB.getStopName(stop_6.getId()));
    }

    // Tests if the Stops are deleted correctly via the deleteStopsInDatabase()
    @Test
    @DisplayName("Stop is deleted successfully")
    public void deleteStopInDatabase_StopDeletedSuccessfully() throws Exception{
        // Arrange
        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Uses the getAllStopsFromDatabase() method to get Stop 1 for deleting
        Stop stop_1 = sqlAdapter.getAllStopsFromDatabase().getFirst();

        // Act
        // Tries to delete stop 1 inside an expression that catches SQLExceptions
        // Since stop 1 is already in use by a Route in route_stops, it should cause a Foreign key constraint
        SQLException thrown = Assertions.assertThrows(SQLException.class, () -> {
            adminSQLAdapter.deleteStopInDatabase(stop_1);
        });

        // Assert
        Assertions.assertEquals("23503", thrown.getSQLState(), "Should be foreign key constraint violation");
        Assertions.assertTrue(thrown.getMessage().contains("Referential integrity constraint violation"));
    }
}
