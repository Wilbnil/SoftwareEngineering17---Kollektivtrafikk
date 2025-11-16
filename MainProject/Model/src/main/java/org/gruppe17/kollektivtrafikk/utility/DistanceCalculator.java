package org.gruppe17.kollektivtrafikk.utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;


// Verktøy klasse for å regne ut lengde og tid.
// Formålet er å kunne regne ut kortest reisetid og avstand i andre klasser.

public class DistanceCalculator {

    private DistanceCalculator() {}

    /**
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    public static double getDistance(double fromX, double fromY, double toX, double toY) {
        // Bruker Pytagoras for å finne lengden som en vektor av x og y lengder.
        double dx = (toX - fromX) * 111.32; //// 1° longitude ≈ 111.32 km
        double dy = (toY - fromY) * 110.57; //// 1° latitude ≈ 110.57 km
        double distance = Math.sqrt(dx * dx + dy * dy);
        return Math.round(distance * 100.0) / 100.0;
    }

    // Regner ut tiden basert på dato- og tidsforskjell.
    public static LocalTime calculateTravelTime(LocalDateTime timeFrom, LocalDateTime timeTo) {
        // Regner ut tiden med Duration klassen.
        Duration travelTime = Duration.between(timeFrom, timeTo);
        // Returnerer tiden som et LocalTime objekt.
        return LocalTime.MIDNIGHT.plus(travelTime);
    }

    // Tentativ løsning for å regne ut kun basert på tid.
    public static LocalTime calculateTravelTime(LocalTime timeFrom, LocalTime timeTo) {
        // Regner ut tiden med Duration klassen.
        Duration travelTime = Duration.between(timeFrom, timeTo);
        // Returnerer tiden som et LocalTime objekt.
        return LocalTime.MIDNIGHT.plus(travelTime);
    }

}
    

