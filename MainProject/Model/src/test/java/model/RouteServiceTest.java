package model;

import gruppe17.kollektivtrafikk.model.Route;
import gruppe17.kollektivtrafikk.service.RouteService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteServiceTest {

    @Test
    public void testGetRouteExists() {
        RouteService service = new RouteService();
        Route route = service.getRoute("Fredrikstad", "Ostfoldhallen");

        assertNotNull(route);
        assertEquals("bus 24", route.getMode());
    }

    @Test
    public void testGetRouteNotExists() {
        RouteService service = new RouteService();
        Route route = service.getRoute("Fredrikstad", "UnknonStop");

        assertNotNull(route);
    }
}
