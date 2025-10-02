package Search;

import Model.Route;
import Service.RouteService;

import java.util.Arrays;

public class SearchEngine {
    private RouteService routeService;

    public SearchEngine() {
        this.routeService = new RouteService();
    }

    public Route search(String query) {

        String[] parts = query.trim().split("\\s+", 2);

        if (parts.length == 2) {
            return routeService.getRoute(parts[0], parts[1]);
        }
        return null;
    }


}
