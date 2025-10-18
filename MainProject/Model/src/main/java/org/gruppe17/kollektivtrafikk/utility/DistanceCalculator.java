package gruppe17.kollektivtrafikk.utility;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalTime;

// Verktøy klasse for å regne ut lengde og tid.
// Formålet er å kunne regne ut kortest reisetid og avstand i andre klasser.

public class DistanceCalculator {

    private DistanceCalculator() {}

    public static double getDistance(double fromX, double fromY, double toX, double toY) {
        // Bruker Pytagoras for å finne lengden som en vektor av x og y lengder.
        double dx = toX - fromX;
        double dy = toY - fromY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static LocalTime calculateTravelTime(LocalDateTime timeFrom, LocalDateTime timeTo) {
        // Regner ut tiden med Duration klassen.
        Duration travelTime = Duration.between(timeFrom, timeTo);
        // Returnerer tiden som et LocalTime objekt.
        return LocalTime.MIDNIGHT.plus(travelTime);
    }



}
