package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Coordinates;
import org.gruppe17.kollektivtrafikk.model.Stop;

public class StopComparison {
    public static Stop finnClosest(Coordinates searchPoint, Stop[] stops) {
        Stop closest = null;
        double minsteAvstand = Double.MAX_VALUE;

        for (Stop stop : stops) {
            Coordinates stopCoord = new Coordinates(stop.getLongitude(), stop.getLatitude());
            double distance = searchPoint.distance(stopCoord);
            if (distance < minsteAvstand) {
                minsteAvstand = distance;
                closest = stop;
            }
        }
        return closest;
    }
}


