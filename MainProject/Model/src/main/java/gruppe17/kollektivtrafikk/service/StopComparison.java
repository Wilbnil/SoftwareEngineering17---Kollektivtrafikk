package gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Coordinates;
import org.gruppe17.kollektivtrafikk.model.DistanceCalculator;
import org.gruppe17.kollektivtrafikk.model.Stop;

public class StopComparison {
    public static Stop finnClosest(Coordinates searchPoint, Stop[] stops) {
        Stop closest = null;
        double minsteAvstand = Double.MAX_VALUE;

        DistanceCalculator calculator = new DistanceCalculator();

        for (Stop stop : stops) {
            Coordinates stopCoord = new Coordinates(stop.getLongitude(), stop.getLatitude());
            double distance = calculator.getDistance(
                    searchPoint.getX(), searchPoint.getY(),
                    stopCoord.getX(), stopCoord.getY()
            );

            if (distance < minsteAvstand) {
                minsteAvstand = distance;
                closest = stop;
            }
        }
        return closest;
    }
}


