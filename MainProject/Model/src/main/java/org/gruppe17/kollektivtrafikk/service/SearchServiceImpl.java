package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;

public class SearchServiceImpl implements SearchService{
    private final RouteService routeService;

    public SearchServiceImpl(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public Route findRoute(String from, String to, boolean onlyWithRoof) {
        Route route = routeService.getRoute(from, to);

        if (route == null) return null;
        if (onlyWithRoof) {
            route.setStops(route.getStopsWithRoof());
        }
        return route;
    }
}
