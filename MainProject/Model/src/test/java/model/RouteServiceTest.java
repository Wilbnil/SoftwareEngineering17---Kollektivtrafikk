package model;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.RouteServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteServiceTest {

    @Test
    public void testGetRouteExists() {
        RouteService service = new RouteServiceImpl();
        Route route = service.getRoute("Fredrikstad", "Ostfoldhallen");

        assertNotNull(route);
        assertEquals("bus 24", route.getMode());
    }

    @Test
    public void testGetRouteNotExists() {
        RouteService service = new RouteServiceImpl();
        Route route = service.getRoute("Fredrikstad", "UnknonStop");

        assertNotNull(route);
    }
}
