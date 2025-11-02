package model;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.SearchService;
import org.gruppe17.kollektivtrafikk.service.SearchServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SearchServiceTest {

    @Test
    public void testFindRouteWithRoofAndDisability() {
        RouteService mockRouteService = new RouteService() {
            @Override
            public Route getRoute(String from, String to) {
                return new Route(1, "TestRoute", Arrays.asList(
                        new Stop(1, "Fredrikstad", "Fredrikstad", 0.0, 0.0, true, true, true),
                        new Stop(2, "Greaker", "Sarpsborg", 0.0, 0.0, false, true, false),
                        new Stop(3, "Amfi Borg", "Sarpsborg", 0.0, 0.0, true, true, true)
                ));
            }
        };

        SearchService searchService = new SearchServiceImpl(mockRouteService);

        Route routeWithRoof = searchService.findRoute("Fredrikstad", "Sarpsborg", true, false);
        assertNotNull(routeWithRoof);
        assertTrue(routeWithRoof.getStops().stream().allMatch(Stop::getRoof));
        assertEquals(2, routeWithRoof.getStops().size());

        Route routeOnlyDisability = searchService.findRoute("Fredrikstad", "Sarpsborg", false, true);
        assertNotNull(routeOnlyDisability);
        assertTrue(routeOnlyDisability.getStops().stream().allMatch(Stop::getDisabilityAccess));
        assertEquals(2, routeOnlyDisability.getStops().size());

        Route routeBoth = searchService.findRoute("Fredrikstad", "Sarpsborg", true, true);
        assertNotNull(routeBoth);
        assertTrue(routeBoth.getStops().stream().allMatch(s -> s.getRoof() && s.getDisabilityAccess()));
        assertEquals(2, routeBoth.getStops().size());
    }
}
