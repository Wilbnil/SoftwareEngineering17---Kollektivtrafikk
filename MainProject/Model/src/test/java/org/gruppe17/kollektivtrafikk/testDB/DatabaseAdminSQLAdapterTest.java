package org.gruppe17.kollektivtrafikk.testDB;

import org.gruppe17.kollektivtrafikk.db.DatabaseAdminSQLAdapter;
import org.gruppe17.kollektivtrafikk.db.DatabaseSQLAdapter;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseAdminSQLAdapterTest {
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
    // Tests if the Routes are inserted correctly via the insertRoutesIntoDatabase() method
    @Test
    @DisplayName("Route is inserted successfully")
    public void insertRouteIntoDatabase_RouteInsertedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

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

        // Act -----------------------------------------------------------------------------

        // Runs the insertRouteIntoDatabase() method
        adminSQLAdapter.insertRouteIntoDatabase(testRoute);

        // Uses the getRouteStartEndStops() method to get the start and end stop in a route
        ArrayList<Integer> startEndStops = testDB.getRouteStartEndStops(testRoute.getId());

        // Assert --------------------------------------------------------------------------

        // Counts the rows in the "routes" table after one route has been added
        // Expects 3 routes since there are 2 from before and 1 added
        Assertions.assertEquals(3, testDB.countRowsInTable("routes"));

        // Checks if the name in the newly inserted Route was inserted correctly
        // If it did, we can assume that the other values were also inserted correctly
        Assertions.assertEquals("Test Route", testDB.getRouteName(3));

        // Checks if the start_stop and end_stop were inserted correctly
        // If the start_stop is 5 and end_stop is 6 it contains the correct stops in order
        Assertions.assertTrue(startEndStops.getFirst() == 5 && startEndStops.getLast() == 6);

        // Checks if the new Route and its corresponding Stops have been inserted into the route_stops table correctly
        // Since the new Route contains 2 Stops, there should be 2 more rows in the route_stops table making it 14
        Assertions.assertEquals(14 , testDB.countRowsInTable("route_stops"));
    }

    // Tests if the Routes are updated correctly via the updateRoutesInDatabase() method
    @Test
    @DisplayName("Route is updated successfully")
    public void updateRouteInDatabase_RouteUpdatedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an object of Route 1 for updating
        Route oldRoute = sqlAdapter.getRouteFromDatabaseWhere(1);

        // Gets and ArrayList of stops for the Route that will be updated
        ArrayList<Stop> testStops = sqlAdapter.getAllStopsFromDatabaseInRoute(oldRoute.getId());

        // Removes the first and last stop to check if the edited Route will be updated correctly
        testStops.remove(testStops.getFirst());
        testStops.remove(testStops.getLast());

        // Act -----------------------------------------------------------------------------

        // Updates Route 1 to be named "Test Route" and to contain the new stops
        adminSQLAdapter.updateRouteInDatabase(oldRoute, new Route(1, "Test Route", testStops));

        // Uses the getRouteStartEndStops() method to get the start and end stop in a route
        ArrayList<Integer> startEndStops = testDB.getRouteStartEndStops(1);

        // Assert --------------------------------------------------------------------------

        // Checks if the name of Route 1 was updated to "Test Route" correctly
        Assertions.assertEquals("Test Route", testDB.getRouteName(1));

        // Checks if the start_stop and end_stop has been updated to correctly reflect the new stops 2, 3, 4 and 5
        // Since 2 is the first stop and 5 is the last, the values we are looking for is 2 and 5
        Assertions.assertTrue(startEndStops.getFirst() == 2 && startEndStops.getLast() == 5);

        // Checks if the route_stops table has been updated to reflect the new Stops in the Route
        // Since stop 1 and 6 is removed from the Route, 2 rows should have been removed from the table and 10 remains
        Assertions.assertEquals(10, testDB.countRowsInTable("route_stops"));
    }

    // Tests if the Routes are deleted correctly via the deleteRouteInDatabase()
    @Test
    @DisplayName("Route is deleted successfully")
    public void deleteRouteInDatabase_RouteDeletedSuccessfully() throws Exception {
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an object of Route 1 for deletion
        Route oldRoute = sqlAdapter.getRouteFromDatabaseWhere(1);

        // Act -----------------------------------------------------------------------------

        // Deletes the route specified above
        adminSQLAdapter.deleteRouteInDatabase(oldRoute);

        // Assert --------------------------------------------------------------------------

        // Checks if there is only 1 Route left in the database
        // There were only 2 Routes and with 1 being deleted, 1 should remain
        Assertions.assertEquals(1, testDB.countRowsInTable("routes"));

        // Checks that all records of Route 1 was removed from the route_stops table
        // Route 1 had 6 rows and the route_stops table had 12 rows
        // Therefore 6 Rows should remain if they were deleted correctly
        Assertions.assertEquals(6, testDB.countRowsInTable("route_stops"));
    }

    // Stops
    // Tests if the Stops are inserted correctly via the insertStopsIntoDatabase() method
    @Test
    @DisplayName("Stop is inserted successfully")
    public void insertStopIntoDatabase_StopInsertedSuccessfully() throws Exception{
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);

        // Creates a new stop to add into the database
        Stop testStop = new Stop(9, "Test Stop 9", "Test Town", 50, 50, true, false);

        // Act -----------------------------------------------------------------------------

        // Inserts the Stop specified above
        adminSQLAdapter.insertStopIntoDatabase(testStop);

        // Assert --------------------------------------------------------------------------

        // Checks if the stops table now contains 9 stops
        // The table contained 8 stops and if the new stop is inserted correctly it should be 9
        Assertions.assertEquals(9, testDB.countRowsInTable("stops"));

        // Checks if the name of the newly inserted Stop is "Test Stop 9" as specified above
        // If this value was inserted correctly, we can assume the other values were too
        Assertions.assertEquals("Test Stop 9", testDB.getStopName(testStop.getId()));
    }

    // Tests if the Stops are updated correctly via the updateStopsInDatabase() method
    @Test
    @DisplayName("Stop is updated successfully")
    public void updateStopInDatabase_StopUpdatedSuccessfully() throws Exception{
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an object of stop 6 which will be updated
        Stop stop_6 = sqlAdapter.getStopFromDatabaseWhere(6);

        // Creates an object of an updated stop 6
        Stop newStop_6 = new Stop(6, "Test Stop 6", "Test Town", 60, 60, true, true);

        // Act -----------------------------------------------------------------------------

        // Updates stop 6 with the new stop 6
        adminSQLAdapter.updateStopInDatabase(stop_6, newStop_6);

        // Assert --------------------------------------------------------------------------

        // Checks if the name of the new Stop 6 was changed to "Test Stop 6" as specified above
        // If this value was updated correctly, we can assume that the other values were too
        Assertions.assertEquals("Test Stop 6", testDB.getStopName(stop_6.getId()));
    }

    // Tests if the Stops are deleted correctly via the deleteStopsInDatabase()
    @Test
    @DisplayName("Stop is deleted successfully")
    public void deleteStopInDatabase_StopDeletedSuccessfully() throws Exception{
        // Arrange -------------------------------------------------------------------------

        // Creates the SQL Adapters connection to test the method
        DatabaseAdminSQLAdapter adminSQLAdapter = new DatabaseAdminSQLAdapter(connection);
        DatabaseSQLAdapter sqlAdapter = new DatabaseSQLAdapter(connection);

        // Creates an object from the stop with id 1 and 7
        Stop stop_1 = sqlAdapter.getStopFromDatabaseWhere(1);
        Stop stop_7 = sqlAdapter.getStopFromDatabaseWhere(7);

        // Act -----------------------------------------------------------------------------

        // Tries to delete stop 1 inside an expression that catches SQLExceptions
        // Since stop 1 is already in use by a Route in route_stops, it should cause a Foreign key constraint
        SQLException thrown = Assertions.assertThrows(SQLException.class, () -> adminSQLAdapter.deleteStopInDatabase(stop_1));

        // Deletes stop 7, should not trigger foreign key check since it is not connected to a Route
        adminSQLAdapter.deleteStopInDatabase(stop_7);

        // Assert --------------------------------------------------------------------------

        // Checks if a foreign key constraint was triggered when trying to delete stop 1
        // This should cause an SQLException and will not delete the stop, since it is in use by a Route
        Assertions.assertTrue(thrown.getMessage().contains("Referential integrity constraint violation"));

        // Checks if there are only 7 stops remaining after stop 7 was deleted
        // Since there were 8 stops before, successfully deleting stop 7 should leave 7 stops in the table
        Assertions.assertEquals(7, testDB.countRowsInTable("stops"));
    }
}

