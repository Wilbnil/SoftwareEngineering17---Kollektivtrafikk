package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;
import org.gruppe17.kollektivtrafikk.model.Stop;

public class StopComparison_OLD {
    public static Stop finnClosest(double searchLongitude, double searchLatitude, Stop[] stops) {
        Stop closest = null;
        double minsteAvstand = Double.MAX_VALUE;

        for (Stop stop : stops) {
            double distance = DistanceCalculator.getDistance(
                    searchLongitude, searchLatitude,
                    stop.getLongitude(), stop.getLatitude()
            );

            if (distance < minsteAvstand) {
                minsteAvstand = distance;
                closest = stop;
            }
        }
        return closest;
    }
}


