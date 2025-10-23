package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.DistanceCalculator;
import org.gruppe17.kollektivtrafikk.model.Stop;

public class StopComparison {
    public static Stop finnClosest(double searchLongitude, double searchLatitude, Stop[] stops) {
        Stop closest = null;
        double minsteAvstand = Double.MAX_VALUE;

        for (Stop stop : stops) {
            double distance = calculator.getDistance(
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


