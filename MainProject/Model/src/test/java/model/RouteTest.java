package model;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

    @Test
    public void testGetStopsWithRoofAndDisability() {
        List<Stop> stops = Arrays.asList(
                new Stop(1, "Fredrikstad", "Fredrikstad", 0.0, 0.0, true, true, true),
                new Stop(2, "Greaker", "Sarpsborg", 0.0, 0.0, false, true, false),
                new Stop(3, "Amfi Borg", "Sarpsborg", 0.0, 0.0, true, true, true)
        );

        Route route = new Route(1, "TestRoute", new ArrayList<>(stops));

        List<Stop> roofStops = route.getStopsWithRoof();
        assertEquals(2, roofStops.size());
        assertTrue(roofStops.stream().allMatch(Stop::getRoof));

        List<Stop> disabilityStops = route.getStops().stream()
                .filter(Stop::getDisabilityAccess)
                .toList();
        assertEquals(2, disabilityStops.size());
        assertTrue(disabilityStops.stream().allMatch(Stop::getDisabilityAccess));
    }
}
