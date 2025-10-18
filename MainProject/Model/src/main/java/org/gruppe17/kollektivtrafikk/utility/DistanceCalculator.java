package org.gruppe17.kollektivtrafikk.utility;

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



}
