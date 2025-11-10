package model;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.StopRepository;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class StopServiceTest {
    @Test
    public void testGetStopsWithRoof_ReasonableValues() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getStopsWithRoof();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getRoof());
        assertTrue(result.get(1).getRoof());
    }

    @Test
    public void testGetStopsWithRoof_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.setEmpty(true);
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getStopsWithRoof();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStopsWithRoof_UnexpectedValues_Exception() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.setThrowException(true);
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getStopsWithRoof();

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
        List<Stop> result = service.getAccessibleStops();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getAccessibility());
        assertTrue(result.get(1).getAccessibility());
    }

    @Test
    public void testGetAccessibleStops_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.setEmpty(true);
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getAccessibleStops();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAccessibleStops_UnexpectedValues_Exception() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.setThrowException(true);
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getAccessibleStops();

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
        List<Stop> result = service.getAllStops();

        // Assert
        assertEquals(4, result.size());
    }

    @Test
    public void testGetAllStops_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.setEmpty(true);
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getAllStops();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllStops_UnexpectedValues_Exception() {
        // Arrange
        FakeStopRepository fakeRepo = new FakeStopRepository();
        fakeRepo.setThrowException(true);
        StopService service = new StopService(fakeRepo);

        // Act
        List<Stop> result = service.getAllStops();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }


    // Fake repository
    private static class FakeStopRepository extends StopRepository {
        private boolean empty = false;
        private boolean throwException = false;

        public FakeStopRepository() {
            super(null);
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public List<Stop> getAll() {
            if (empty) {
                return new ArrayList<>();
            }

            if (throwException) {
                throw new RuntimeException();
            }

            List<Stop> stops = new ArrayList<>();

            stops.add(new Stop(1, "Fredrikstad Bussterminal", "Fredrikstad", 24.2, 4.6, true, true));
            stops.add(new Stop(2, "Sarsborg Bussterminal", "Sarpsborg", 30.1, 7.3, true, false));
            stops.add(new Stop(3, "AMFI Borg", "Sarpsborg", 36.1, 10.3, false, true));
            stops.add(new Stop(4, "Torsbekken", "Sarpsborg", 45.1, 11.3, false, false));

            return stops;
        }
    }
}