package org.gruppe17.kollektivtrafikk.service;
import org.gruppe17.kollektivtrafikk.model.Route;

public interface SearchService {
    Route findRoute(String from, String to, boolean onlyWithRoof);
}
