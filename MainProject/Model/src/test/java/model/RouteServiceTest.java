package model;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.service.RouteService_OLD;
import org.gruppe17.kollektivtrafikk.service.RouteServiceImpl_OLD;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteServiceTest {

    @Test
    public void testGetRouteExists() {
        RouteService_OLD service = new RouteServiceImpl_OLD();
        Route route = service.getRoute("Fredrikstad", "Ostfoldhallen");

        assertNotNull(route);
        assertEquals("bus 24", route.getType());
    }

    @Test
    public void testGetRouteNotExists() {
        RouteService_OLD service = new RouteServiceImpl_OLD();
        Route route = service.getRoute("Fredrikstad", "UnknonStop");

        assertNotNull(route);
    }
}
