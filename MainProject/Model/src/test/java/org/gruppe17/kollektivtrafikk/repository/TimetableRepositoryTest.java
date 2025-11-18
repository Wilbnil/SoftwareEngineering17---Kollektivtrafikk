package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.testDB.H2TestDatabase;
import org.gruppe17.kollektivtrafikk.testDB.TestDatabase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;

public class TimetableRepositoryTest {
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
    @DisplayName("Timetable returned successfully from id")
    public void getTimetableById_TimetableReturnedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Timetable idealTimetable = new Timetable(1, 1, "monday", LocalTime.of(5, 30), LocalTime.of(00, 25), 10);
        Timetable returnedTimetable;

        // Act
        returnedTimetable = timetableRepo.getById(1);

        //Assert
        Assertions.assertEquals(idealTimetable.getRoute_id(), returnedTimetable.getRoute_id());
        Assertions.assertEquals(idealTimetable.getDay_of_week(), returnedTimetable.getDay_of_week());
    }

    @Test
    @DisplayName("Timetable returned successfully from route and day")
    public void getTimetableRouteDay_TimetableReturnedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route routeInIdealTimetable = new Route(4, "RE20", "tog");
        Timetable idealTimetable = new Timetable(16, 4, "tuesday", LocalTime.of(6, 11), LocalTime.of(0, 22), 60);
        Timetable returnedTimetable;

        // Act
        returnedTimetable = timetableRepo.getTimetableRouteDay(routeInIdealTimetable, "tuesday");

        //Assert
        Assertions.assertEquals(idealTimetable.getId(), returnedTimetable.getId());
        Assertions.assertEquals(idealTimetable.getTimeInterval(), returnedTimetable.getTimeInterval());
    }

    @Test
    @DisplayName("All timetables returned successfully")
    public void getAllTimetables_TimetablesReturnedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        ArrayList<Timetable> returnedTimetables;

        // Act
        returnedTimetables = timetableRepo.getAll();

        //Assert
        Assertions.assertEquals(21, returnedTimetables.size());
        Assertions.assertEquals(1, returnedTimetables.getFirst().getId());
        Assertions.assertEquals(21, returnedTimetables.getLast().getId());
    }

    @Test
    @DisplayName("Timetables in route returned successfully")
    public void getTimetablesInRoute_TimetableReturnedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route route = new Route(2, "2", "buss");

        ArrayList<Timetable> returnedTimetables;

        // Act
        returnedTimetables = timetableRepo.getTimetablesInRoute(route);

        //Assert
        Assertions.assertEquals(7, returnedTimetables.size());
        Assertions.assertEquals(8, returnedTimetables.getFirst().getId());
        Assertions.assertEquals(14, returnedTimetables.getLast().getId());
    }

    @Test
    @DisplayName("Timetable inserted successfully")
    public void insertTimetable_TimetableInsertedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Timetable insertedTimetable = new Timetable(22, 3, "monday", LocalTime.of(1, 1), LocalTime.of(1, 1), 1);

        // Act
        timetableRepo.insert(insertedTimetable);

        //Assert
        Assertions.assertEquals(22, testDB.countRowsInTable("timetables"));
        Assertions.assertEquals("monday", testDB.getDayOfWeekFromId(22));
    }

    @Test
    @DisplayName("Timetable updated successfully")
    public void updateTimetable_TimetableUpdatedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Timetable newTimetable = new Timetable(1, 1, "monday", LocalTime.of(4, 0), LocalTime.of(1, 30), 20);
        Timetable oldTimetable = new Timetable(1, 1, "monday", LocalTime.of(5, 30), LocalTime.of(0, 25), 10);

        // Act
        timetableRepo.update(oldTimetable, newTimetable);

        //Assert
        Assertions.assertEquals(LocalTime.of(4, 0), testDB.getFirstTimeFromId(1));
    }

    @Test
    @DisplayName("Timetable deleted successfully")
    public void deleteTimetable_TimetableDeletedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Timetable deletedTimetable = new Timetable(1, 1, "monday", LocalTime.of(4, 0), LocalTime.of(1, 30), 20);

        // Act
        timetableRepo.delete(deletedTimetable);

        //Assert
        Assertions.assertEquals(20, testDB.countRowsInTable("timetables"));
    }

    @Test
    @DisplayName("Timetables deleted successfully")
    public void deleteTimetablesInRoute_TimetablesDeletedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route route = new Route(2, "2", "buss");

        // Act
        timetableRepo.deleteTimetablesInRoute(route);

        //Assert
        Assertions.assertEquals(14, testDB.countRowsInTable("timetables"));
    }

    @Test
    @DisplayName("Time from start returned successfully")
    public void getRouteStopTime_RouteStopTimeReturnedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);
        int time_from_start;

        Route route = new Route(2, "2", "buss");
        Stop stop = new Stop(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139, 10.9403, false, false);

        // Act
        time_from_start = timetableRepo.getRouteStopTime(route, stop);

        //Assert
        Assertions.assertEquals(25, time_from_start);
    }

    @Test
    @DisplayName("RouteStopTime inserted successfully")
    public void insertRouteStopTime_RouteStopTimeInsertedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route route = new Route(3, "630", "buss");
        Stop stop = new Stop(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139, 10.9403, false, false);
        int insertedTime_from_start = 10;

        // Act
        timetableRepo.insertRouteStopTime(route, stop, insertedTime_from_start);

        //Assert
        Assertions.assertEquals(10, testDB.getTimeFromStartFromRouteStop(route, stop));
    }

    @Test
    @DisplayName("RouteStopTime updated successfully")
    public void updateRouteStopTime_RouteStopTimeUpdatedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route route = new Route(2, "2", "buss");
        Stop stop = new Stop(1, "Fredrikstad bussterminal", "Fredrikstad", 59.2139, 10.9403, false, false);
        int newTime_from_start = 100;

        // Act
        timetableRepo.updateRouteStopTime(route, stop, newTime_from_start);

        //Assert
        Assertions.assertEquals(100, testDB.getTimeFromStartFromRouteStop(route, stop));
    }

    @Test
    @DisplayName("RouteStopTime deleted successfully")
    public void deleteRouteStopTime_RouteStopTimeDeletedSuccessfully() throws Exception {
        // Arrange
        TimetableRepository timetableRepo = new TimetableRepository(connection);

        Route route = new Route(2, "2", "buss");

        // Act
        timetableRepo.deleteRouteStopTime(route);

        //Assert
        Assertions.assertEquals(14, testDB.countRowsInTable("route_stop_time"));
    }
}
