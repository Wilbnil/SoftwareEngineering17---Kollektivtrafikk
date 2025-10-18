package gruppe17.kollektivtrafikk.search;

import gruppe17.kollektivtrafikk.model.Route;
import gruppe17.kollektivtrafikk.service.RouteService;

//Handles search functionality for transport routes based on user queries.

public class SearchEngine {
    private RouteService routeService;

    //Constructs a new SearchEngine with a RouteService instance.
    public SearchEngine() {
        this.routeService = new RouteService();
    }

    //Searches for a route based on a query string.
    public Route search(String query) {

        String[] parts = query.trim().split("\\s+", 2); //Split on first space to handle multi-word stops

        if (parts.length == 2) {
            return routeService.getRoute(parts[0], parts[1]);
        }
        return null;
    }


}
