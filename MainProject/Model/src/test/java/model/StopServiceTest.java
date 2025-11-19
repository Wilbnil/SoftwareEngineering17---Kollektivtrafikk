package model;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.StopRepository;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StopServiceTest {

    @Test
    public void testGetStopsWithRoof_ReasonableValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getStopsWithRoof();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getRoof());
        assertTrue(result.get(1).getRoof());
    }

    @Test
    public void testGetStopsWithRoof_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.empty = true;
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getStopsWithRoof();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStopsWithRoof_UnexpectedValues_Exception() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.throwError = true;
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getStopsWithRoof();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAccessibleStops_ReasonableValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getAccessibleStops();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getAccessibility());
        assertTrue(result.get(1).getAccessibility());
    }

    @Test
    public void testGetAccessibleStops_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.empty = true;
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getAccessibleStops();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAccessibleStops_UnexpectedValues_Exception() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.throwError = true;
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getAccessibleStops();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllStops_ReasonableValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getAllStops();

        // Assert
        assertEquals(4, result.size());
    }

    @Test
    public void testGetAllStops_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.empty = true;
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getAllStops();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllStops_UnexpectedValues_Exception() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.throwError = true;
        StopService service = new StopService(fakeRepo);

        // Act
        ArrayList<Stop> result = service.getAllStops();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetStopById_ReasonableValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        Stop result = service.getStopById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Fredrikstad Bussterminal", result.getName());
    }

    @Test
    public void testGetStopById_UnexpectedValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        Stop resultNonExistent = service.getStopById(67);

        // Assert
        assertNull(resultNonExistent);
    }

    @Test
    public void testGetStopByName_ReasonableValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        Stop result = service.getStopByName("AMFI Borg");

        // Assert
        assertNotNull(result);
        assertEquals("AMFI Borg", result.getName());
        assertEquals(3, result.getId());
    }

    @Test
    public void testGetStopByName_UnexpectedValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        Stop resultNonExistent = service.getStopByName("Nonexistent Stop");

        // Assert
        assertNull(resultNonExistent);
    }

    @Test
    @DisplayName("Nearest stop returned successfully")
    public void getAccegetNearestStopWithRoof_NearestStopReturnedSuccessfully() {
        // Arramge
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService stopService = new StopService(fakeRepo);

        Stop stopWithoutRoof = new Stop(3, "AMFI Borg", "Sarpsborg", 36.1, 10.3, false, true);

        // Act
        Stop stopWithRoof = stopService.getNearestStopWithRoof(stopWithoutRoof);

        // Assert
        assertTrue(stopWithRoof.getRoof());
        assertEquals("Sarpsborg Bussterminal" ,stopWithRoof.getName());
    }

    @Test
    @DisplayName("Nearest stop returned successfully")
    public void getAccegetNearestStopWithAccessibility_NearestStopReturnedSuccessfully() {
        // Arramge
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService stopService = new StopService(fakeRepo);

        Stop stopWithoutAccessibility = new Stop(2, "Sarpsborg Bussterminal", "Sarpsborg", 30.1, 7.3, true, false);

        // Act
        Stop stopWithAccessibility = stopService.getNearestStopWithAccessibility(stopWithoutAccessibility);

        // Assert
        assertTrue(stopWithAccessibility.getAccessibility());
        assertEquals("Fredrikstad Bussterminal" ,stopWithAccessibility.getName());
    }

    // Fake repository
    class FakeStopRepository extends StopRepository {
        public boolean empty = false;
        public boolean throwError = false;

        FakeStopRepository() {
            super(null);
        }

        public ArrayList<Stop> getAll() throws Exception {
            if (throwError) {
                throw new Exception("Error");
            }
            if (empty) {
                return new ArrayList<>();
            }

            ArrayList<Stop> stops = new ArrayList<>();
            stops.add(new Stop(1, "Fredrikstad Bussterminal", "Fredrikstad", 24.2, 4.6, true, true));
            stops.add(new Stop(2, "Sarpsborg Bussterminal", "Sarpsborg", 30.1, 7.3, true, false));
            stops.add(new Stop(3, "AMFI Borg", "Sarpsborg", 36.1, 10.3, false, true));
            stops.add(new Stop(4, "Torsbekken", "Sarpsborg", 45.1, 11.3, false, false));
            return stops;
        }

        public Stop getById(int id) throws Exception {
            if (throwError) {
                throw new Exception("Error");
            }
            if (empty) {
                return null;
            }

            ArrayList<Stop> stops = getAll();
            for (Stop stop : stops) {
                if (stop.getId() == id) {
                    return stop;
                }
            }
            return null;
        }

        public Stop getByName(String name) throws Exception {
            if (throwError) {
                throw new Exception("Error");
            }
            if (empty) {
                return null;
            }

            ArrayList<Stop> stops = getAll();
            for (Stop stop : stops) {
                if (stop.getName().equals(name)) {
                    return stop;
                }
            }
            return null;
        }
    }
}