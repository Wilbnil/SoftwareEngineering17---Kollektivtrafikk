package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.testDB.H2TestDatabase;
import org.gruppe17.kollektivtrafikk.testDB.TestDatabase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class StopRepositoryTest {
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
    @DisplayName("Stop is returned successfully from id")
    public void getStopById_StopReturnedSuccessfully() throws Exception {
        //Arrange
        StopRepository stopRepo = new StopRepository(connection);

        Stop idealStop = new Stop(10, "Fredrikstad stasjon", "Fredrikstad",59.2089, 10.9506, false, false);
        Stop returnedStop;

        //Act
        returnedStop = stopRepo.getById(10);

        //Assert
        Assertions.assertEquals(idealStop.getName(), returnedStop.getName());
        Assertions.assertEquals(idealStop.getRoof(), returnedStop.getRoof());
    }

    @Test
    @DisplayName("Stop is returned successfully from name")
    public void getStopByName_StopReturnedSuccessfully() throws Exception {
        //Arrange
        StopRepository stopRepo = new StopRepository(connection);

        Stop idealStop = new Stop(15, "Oslo S", "Oslo", 59.9111, 10.7535, false, false);
        Stop returnedStop;

        //Act
        returnedStop = stopRepo.getByName("Oslo S");

        //Assert
        Assertions.assertEquals(idealStop.getId(), returnedStop.getId());
        Assertions.assertEquals(idealStop.getAccessibility(), returnedStop.getAccessibility());
    }

    @Test
    @DisplayName("All stops returned successfully")
    public void getAllStops_StopsReturnedSuccessfully() throws Exception {
        //Arrange
        StopRepository stopRepo = new StopRepository(connection);

        ArrayList<Stop> returnedStops;

        //Act
        returnedStops = stopRepo.getAll();

        //Assert
        Assertions.assertEquals(16, returnedStops.size());
        Assertions.assertEquals(1, returnedStops.getFirst().getId());
        Assertions.assertEquals(16, returnedStops.getLast().getId());
    }

    @Test
    @DisplayName("Stop is inserted successfully")
    public void insertStop_StopInsertedSuccessfully() throws Exception {
        //Arrange
        StopRepository stopRepo = new StopRepository(connection);

        Stop newStop = new Stop(17, "New cool stop", "Newland", 10, 10, true, false);

        //Act
        stopRepo.insert(newStop);

        //Assert
        Assertions.assertEquals(17, testDB.countRowsInTable("stops"));
        Assertions.assertEquals("New cool stop", testDB.getStopNameFromId(17));
    }

    @Test
    @DisplayName("Stop is updated successfully")
    public void updateStop_StopUpdatedSuccessfully() throws Exception {
        //Arrange
        StopRepository stopRepo = new StopRepository(connection);

        Stop oldStop = new Stop(9, "Sarpsborg stasjon", "Sarpsborg", 59.2861, 11.118, false, false);
        Stop newStop = new Stop(9, "Updated station", "Better than særp at least", 10, 10, true, true);

        //Act
        stopRepo.update(oldStop, newStop);

        //Assert
        Assertions.assertEquals("Updated station", testDB.getStopNameFromId(9));
    }

    @Test
    @DisplayName("Stop is deleted successfully")
    public void deleteStop_StopDeletedSuccessfully() throws Exception {
        //Arrange
        StopRepository stopRepo = new StopRepository(connection);

        Stop deletedStop = new Stop(16, "Test stop", "Test", 10, 10, true, false);

        Stop deletedStopWithForeignKey = new Stop(6, "Sarpsborg bussterminal", "Sarpsborg", 59.283, 11.1071, false, false);

        //Act
        stopRepo.delete(deletedStop);
        SQLException thrown = Assertions.assertThrows(SQLException.class, () -> stopRepo.delete(deletedStopWithForeignKey));

        //Assert
        Assertions.assertEquals(15, testDB.countRowsInTable("stops"));
        Assertions.assertTrue(thrown.getMessage().contains("Referential integrity constraint violation"));
    }
}
