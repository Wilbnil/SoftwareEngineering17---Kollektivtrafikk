package model;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.utility.AdminTemporaryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AdminTemporaryStorageTest {


    @BeforeEach
    void setup() {
        //delete each Json before start of new test
        File file = new File("routes.json");
        if (file.exists()) {
            file.delete();
        }
        AdminTemporaryStorage.getRoutes().clear();
    }


    @Test
    void testAddRouteAndRetrieve() {
        Route route = new Route(0, "Test Route");
        AdminTemporaryStorage.addRoute(route);

        assertEquals(1, AdminTemporaryStorage.getRoutes().size());
        assertEquals("Test Route", AdminTemporaryStorage.getRoutes().get(0).getName());
    }

    @Test
    void testSaveAndLoadFromFile() {
        Route route = new Route(0, "Route A");
        AdminTemporaryStorage.addRoute(route);

        AdminTemporaryStorage.loadFromFile();

        assertFalse(AdminTemporaryStorage.getRoutes().isEmpty());
        assertEquals("Route A", AdminTemporaryStorage.getRoutes().get(0).getName());
    }
}
