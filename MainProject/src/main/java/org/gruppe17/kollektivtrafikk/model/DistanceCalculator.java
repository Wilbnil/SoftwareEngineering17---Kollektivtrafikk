package org.gruppe17.kollektivtrafikk.model;

public class DistanceCalculator {
    private double distance;

    public DistanceCalculator() {
        this.distance = 0.0;
    }

    public void calculateDistance(Coordinates from, Coordinates to) {
        this.distance = from.distance(to); //uses method from Coordinates
    }

    public double getDistance() {
        return distance;
    }

    public double calculateTravelTime(double distance, double averageSpeedKmH) {
        return distance / averageSpeedKmH * 60; //time in mins
    }



}
