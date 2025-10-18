package gruppe17.kollektivtrafikk.model;

public class DistanceCalculator {

    public DistanceCalculator() {

    }

    public double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calculateTravelTime(double distance, double averageSpeedKmH) {
        return distance / averageSpeedKmH * 60; //time in mins
    }



}
