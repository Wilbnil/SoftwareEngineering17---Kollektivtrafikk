package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.testDB.H2TestDatabase;
import org.gruppe17.kollektivtrafikk.testDB.TestDatabase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;

public class RouteRepositoryTest {
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

    @Test
    @DisplayName("Route returned successfully from id")
    public void getRouteById_RouteReturnedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);

        Route idealRoute = new Route(1, "1", "buss");
        Route returnedRoute;

        // Act
        returnedRoute = routeRepo.getById(1);

        // Assert
        Assertions.assertEquals(idealRoute.getName(), returnedRoute.getName());
        Assertions.assertEquals(idealRoute.getType(), returnedRoute.getType());
        Assertions.assertEquals(6, returnedRoute.getStops().size());
    }

    @Test
    @DisplayName("Route returned successfully from name")
    public void getRouteByName_RouteReturnedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);

        Route idealRoute = new Route(1, "1", "buss");
        Route returnedRoute;

        // Act
        returnedRoute = routeRepo.getByName("1");

        // Assert
        Assertions.assertEquals(idealRoute.getId(), returnedRoute.getId());
        Assertions.assertEquals(idealRoute.getType(), returnedRoute.getType());
        Assertions.assertEquals(6, returnedRoute.getStops().size());
    }

    @Test
    @DisplayName("All routes returned successfully")
    public void getAllRoutes_RoutesReturnedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);

        ArrayList<Route> returnedRoutes;

        // Act
        returnedRoutes = routeRepo.getAll();

        // Assert
        Assertions.assertEquals(4, returnedRoutes.size());
        Assertions.assertEquals(1, returnedRoutes.getFirst().getId());
        Assertions.assertEquals(4, returnedRoutes.getLast().getId());
    }

    @Test
    @DisplayName("All routes returned successfully from/to")
    public void getAllRoutesFromTo_RoutesReturnedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);

        int start_stop = 5;
        int end_stop = 2;

        ArrayList<Route> returnedRoutes;

        // Act
        returnedRoutes = routeRepo.getAllFromTo(start_stop, end_stop);

        // Assert
        Assertions.assertEquals(1, returnedRoutes.size());
        Assertions.assertEquals(2, returnedRoutes.getFirst().getId());
        Assertions.assertEquals(6, returnedRoutes.getFirst().getStops().size());
    }

    @Test
    @DisplayName("All stops in route returned successfully")
    public void getAllStopsInRoute_StopsReturnedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);

        ArrayList<Stop> returnedStops;

        // Act
        returnedStops = routeRepo.getAllStopsInRoute(1);

        // Assert
        Assertions.assertEquals(6, returnedStops.size());
        Assertions.assertEquals(1, returnedStops.getFirst().getId());
        Assertions.assertEquals(6, returnedStops.getLast().getId());
    }

    @Test
    @DisplayName("Route inserted Successfully")
    public void insertRoute_RouteInsertedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);

        Stop stop1 = new Stop(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139, 10.9403, false, false);
        Stop stop2 = new Stop(2, "Østfoldhallen", "Fredrikstad", 59.2516, 10.9931, false, false);
        Stop stop3 = new Stop(3, "Greåker", "Sarpsborg", 59.2661, 11.0349, false, false);

        ArrayList<Stop> stops = new ArrayList<>();

        stops.add(stop1);
        stops.add(stop2);
        stops.add(stop3);

        Route newRoute = new Route(5, "Test Route", stops, "Tog");

        // Act
        routeRepo.insert(newRoute);
        routeRepo.insertRouteStops(newRoute);

        // Assert
        Assertions.assertEquals(5, testDB.countRowsInTable("routes"));
        Assertions.assertEquals(30, testDB.countRowsInTable("route_stops"));
        Assertions.assertEquals("Test Route", testDB.getRouteName(5));
    }

    @Test
    @DisplayName("Route updated Successfully")
    public void updateRoute_RouteUpdatedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Stop stop1 = new Stop(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139, 10.9403, false, false);
        Stop stop2 = new Stop(2, "Østfoldhallen", "Fredrikstad", 59.2516, 10.9931, false, false);
        Stop stop3 = new Stop(3, "Greåker", "Sarpsborg", 59.2661, 11.0349, false, false);
        Stop stop4 = new Stop(4, "AMFI Borg", "Sarpsborg", 59.2741, 11.0822, false, false);
        Stop stop5 = new Stop(5, "Torsbekken", "Sarpsborg", 59.284, 11.0984, false, false);
        Stop stop6 = new Stop(6, "Sarpsborg bussterminal", "Sarpsborg", 59.283, 11.1071, false, false);

        ArrayList<Stop> oldRouteStops = new ArrayList<>();
        ArrayList<Stop> newRouteStops = new ArrayList<>();

        oldRouteStops.add(stop1);
        oldRouteStops.add(stop2);
        oldRouteStops.add(stop3);
        oldRouteStops.add(stop4);
        oldRouteStops.add(stop5);
        oldRouteStops.add(stop6);

        newRouteStops.add(stop3);
        newRouteStops.add(stop4);
        newRouteStops.add(stop5);


        Route newRoute = new Route(1, "Updated Route", newRouteStops, "tog");
        Route oldRoute = new Route(1, "1", oldRouteStops, "buss");

        // Act
        timetableRepo.deleteRouteStopTime(oldRoute);
        timetableRepo.deleteTimetablesInRoute(oldRoute);
        routeRepo.deleteRouteStops(oldRoute);
        routeRepo.update(oldRoute, newRoute);
        routeRepo.insertRouteStops(newRoute);

        // Assert
        Assertions.assertEquals("Updated Route", testDB.getRouteName(1));
        Assertions.assertEquals(24, testDB.countRowsInTable("route_stops"));
        Assertions.assertEquals(14, testDB.countRowsInTable("route_stop_time"));
        Assertions.assertEquals(14, testDB.countRowsInTable("timetables"));
    }

    @Test
    @DisplayName("Route deleted Successfully")
    public void deleteRoute_RouteDeletedSuccessfully() throws Exception {
        // Arrange
        RouteRepository routeRepo = new RouteRepository(connection);
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route deleteRoute = new Route(1, "1", "buss");

        // Act
        timetableRepo.deleteRouteStopTime(deleteRoute);
        timetableRepo.deleteTimetablesInRoute(deleteRoute);
        routeRepo.deleteRouteStops(deleteRoute);
        routeRepo.delete(deleteRoute);

        // Assert
        Assertions.assertEquals(3, testDB.countRowsInTable("routes"));
        Assertions.assertEquals(21, testDB.countRowsInTable("route_stops"));
        Assertions.assertEquals(14, testDB.countRowsInTable("route_stop_time"));
        Assertions.assertEquals(14, testDB.countRowsInTable("timetables"));
    }
}
