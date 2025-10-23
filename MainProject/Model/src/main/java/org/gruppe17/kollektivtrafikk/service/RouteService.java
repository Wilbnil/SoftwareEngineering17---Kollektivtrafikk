package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Route;

public interface RouteService {
    Route getRoute(String from, String to);
}
