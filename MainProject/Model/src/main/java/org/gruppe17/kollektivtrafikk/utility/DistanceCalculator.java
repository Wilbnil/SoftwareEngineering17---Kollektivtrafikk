package org.gruppe17.kollektivtrafikk.utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;


/**
 * The {@code DistanceCalculator} class is a utility class that bring methods for calculating travel time and distance
 * together in one class. Being a utility class it should not be instantiated or inherited from. It is set as a final
 * class so the class cannot be inherited.
 * Here is an example of how it can be used:
 * <blockquote><pre>
 *     DistanceCalculator.doDesiredMethod(parameters);
 * </pre></blockquote><p>
 * Here is an example of how the class should <font color="E83838">NOT</font>  be used:
 * <blockquote><pre>
 *     DistanceCalculator distanceCalculator = new distanceCalculator();
 *
 *     distanceCalculator.doDesiredMethod(parameters);
 * </pre></blockquote><p>
 *
 * This class uses {@code Java.time} classes to help calculate time.
 */

public final class DistanceCalculator {

    // Constructor should not be used. As such it is set to private.
    private DistanceCalculator() {}

    /**
     * Returns the distance between two locations on a two-dimensional plane in kilometers. The method uses coordinates to
     * calculate the distance, which is represented by the four parameters. It doesn't matter which direction
     * is considered x and y, as long as it is used consistently between the two locations. Which location is considered
     * to and from also doesn't matter as long as the fromX and Y and toX and Y respectively, belong to the same location.
     *
     * <p>
     *
     * </p>
     *
     * parameters:
     * @param fromX A coordinate belonging to the first location in an undetermined direction, but it must be consistent with toX.
     * @param fromY A coordinate belonging to the first location in an undetermined direction, but it must be consistent with toY.
     * @param toX A coordinate belonging to the second location in an undetermined direction, but it must be consistent with fromX.
     * @param toY A coordinate belonging to the second location in an undetermined direction, but it must be consistent with fromY.
     * @return The distance between the two locations in kilometers as rounded with to decimals.
     */
    public static double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        return Math.sqrt(dx * dx + dy * dy);
    }


    /**
     * Calculates the time between two given {@code LocalDateTime} objects.
     * It can be used to get the travel time between two locations.
     * This method will work for times that crosses midnight.
     * <p>
     * Note that unlike the {@code getDistance} method, which time is set to from and to will impact the result.
     * In normal use, you would always take the more recent time as the timeTo and the older time as timeFrom.
     * </p>
     *
     * @param timeFrom A {@code LocalDateTime} object that you want to calculate the time from.
     * @param timeTo A {@code LocalDateTime} object that you want to calculate the time to.
     * @return A {@code LocalTime} object representing the time between the two given dates and times.
     */

    public static LocalTime calculateTravelTime(LocalDateTime timeFrom, LocalDateTime timeTo) {

        Duration travelTime = Duration.between(timeFrom, timeTo);

        return LocalTime.MIDNIGHT.plus(travelTime);
    }

    /**
     * Calculates the time between two given {@code LocalTime} objects. This method is overloaded
     * It can be used to get the travel time between two locations.
     * This method will not work for times that crosses midnight.
     * <p>
     * Note that unlike the {@code getDistance} method, which time is set to from and to will impact the result.
     * In normal use, you would always take the more recent time as the timeTo and the older time as timeFrom.
     * </p>
     * @param timeFrom A {@code LocalTime} object that you want to calculate the time from.
     * @param timeTo A {@code LocalTime} object that you want to calculate the time to.
     * @return A {@code LocalTime} object representing the time between the two given times.
     */


    public static LocalTime calculateTravelTime(LocalTime timeFrom, LocalTime timeTo) {

        Duration travelTime = Duration.between(timeFrom, timeTo);

        return LocalTime.MIDNIGHT.plus(travelTime);
    }

}
    

