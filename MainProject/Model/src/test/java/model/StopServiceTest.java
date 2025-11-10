package model;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.RepositoryStop;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.testDB.H2TestDatabase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StopServiceTest {

    private H2TestDatabase testDB;
    private Connection connection;
    private RepositoryStop repository;
    private StopService service;

    @BeforeEach
    public void setUp() throws Exception {
        testDB = new H2TestDatabase();
        connection = testDB.startDB();
        testDB.createTables();
        repository = new RepositoryStop(connection);
        service = new StopService(repository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        testDB.stopDB();
    }

    @Test
    public void testGetStopsWithRoof_ReasonableValues() throws Exception {
        // Arrange
        Stop stop1 = new Stop(1, "Fredrikstad Bussterminal", "Fredrikstad", 24.2, 4.6, true, true);
        Stop stop2 = new Stop(2, "Sarsborg Bussterminal", "Sarpsborg", 30.1, 7.3, false, true);
        Stop stop3 = new Stop(3, "AMFI Borg", "Sarpsborg", 36.1, 10.3, true, false);
        Stop stop4 = new Stop(4, "Torsbekken", "Sarpsborg", 45.1, 11.3, false, false);

        repository.insert(stop1);
        repository.insert(stop2);
        repository.insert(stop3);
        repository.insert(stop4);

        // Act
        List<Stop> result = service.getStopsWithRoof();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getRoof());
        assertTrue(result.get(1).getRoof());
    }

    @Test
    public void testGetAccessibleStops_ReasonableValues() throws Exception {
        // Arrange
        Stop stop1 = new Stop(1, "Fredrikstad Bussterminal", "Fredrikstad", 24.2, 4.6, true, true);
        Stop stop2 = new Stop(2, "Sarsborg Bussterminal", "Sarpsborg", 30.1, 7.3, false, true);
        Stop stop3 = new Stop(3, "AMFI Borg", "Sarpsborg", 36.1, 10.3, true, false);
        Stop stop4 = new Stop(4, "Torsbekken", "Sarpsborg", 45.1, 11.3, false, false);

        repository.insert(stop1);
        repository.insert(stop2);
        repository.insert(stop3);
        repository.insert(stop4);

        // Act
        List<Stop> result = service.getAccessibleStops();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getAccessibility());
        assertTrue(result.get(1).getAccessibility());
    }

    @Test
    public void testGetAllStops_ReasonableValues() throws Exception {
        // Arrange
        Stop stop1 = new Stop(1, "Fredrikstad Bussterminal", "Fredrikstad", 24.2, 4.6, true, true);
        Stop stop2 = new Stop(2, "Sarsborg Bussterminal", "Sarpsborg", 30.1, 7.3, false, true);
        Stop stop3 = new Stop(3, "AMFI Borg", "Sarpsborg", 36.1, 10.3, true, false);
        Stop stop4 = new Stop(4, "Torsbekken", "Sarpsborg", 45.1, 11.3, false, false);

        repository.insert(stop1);
        repository.insert(stop2);
        repository.insert(stop3);
        repository.insert(stop4);

        // Act
        List<Stop> result = service.getAllStops();

        // Assert
        assertEquals(4, result.size());
    }

    @Test
    public void testGetStopsWithRoof_UnexpectedValues() {
        // Arrange - Tom database
        // Act
        List<Stop> result1 = service.getStopsWithRoof();

        // Assert
        assertEquals(0, result1.size());
    }

    @Test
    public void testGetAccessibleStops_UnexpectedValues() {
        // Arrange - Tom database
        // Act
        List<Stop> result1 = service.getAccessibleStops();

        // Assert
        assertEquals(0, result1.size());
    }

    @Test
    public void testGetAllStops_UnexpectedValues() {
        // Arrange - Tom database
        // Act
        List<Stop> result1 = service.getAllStops();

        // Assert
        assertEquals(0, result1.size());
    }
}