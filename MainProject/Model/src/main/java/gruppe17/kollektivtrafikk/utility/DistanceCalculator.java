package gruppe17.kollektivtrafikk.utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import gruppe17.kollektivtrafikk.model.StopData;

public class DistanceCalculator {

    private DistanceCalculator() {

    }

    public static double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double calculateTravelTime(double distance, double averageSpeedKmH) {
        return distance / averageSpeedKmH * 60; //time in mins
    }

    public static LocalTime calculateTravelTime(LocalDateTime timeFrom, LocalDateTime timeTo) {
        // Regner ut tiden med Duration klassen.
        Duration travelTime = Duration.between(timeFrom, timeTo);
        // Returnerer tiden som et LocalTime objekt.
        return LocalTime.MIDNIGHT.plus(travelTime);
    }


    public static double getDistanceFromStops(String fromName, String toName) {
        var stops = StopData.getStops();
        var from = stops.stream().filter(stop -> stop.getName().equals(fromName)).findFirst();
        var to = stops.stream().filter(stop -> stop.getName().equals(toName)).findFirst();
        if (from.isPresent() && to.isPresent()) {
            return getDistance(from.get().getLongitude(), from.get().getLatitude(),
                    to.get().getLongitude(), to.get().getLatitude());
        }
     return-1;
}


}
