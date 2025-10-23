package model;

import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceCalculatorTest {

    @Test
    public void testDistanceCalculatorTravelTime_ReasonableValues() {
        // Arrange
        LocalDateTime timeFrom = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30));
        LocalDateTime timeTo = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0));

        // Act
        LocalTime time = DistanceCalculator.calculateTravelTime(timeFrom, timeTo);

        // Assert
        assertEquals(LocalTime.of(0, 30), time);
    }
    // Opplagte testverdier:
    @Test
    public void testDistanceCalculatorDistance_ReasonableValues() {
        //Act
        double distance = DistanceCalculator.getDistance(0.0, 0.0, 3.0, 0.0);

        // Assert
        assertEquals(3, distance);
    }

    // Mindre forventede verdier:
    @Test
    public void testDistanceCalculatorDistance_UnexpectedValues() {
        //Act
        double distance1 = DistanceCalculator.getDistance(2.0, 0.0, 2.0, 0.0);
        double distance2 = DistanceCalculator.getDistance(0.0, 0.0, 0.0, 0.0);
        double distance3 = DistanceCalculator.getDistance(0.1, 0.1, 0.1, 0.1);

        double distance4 = DistanceCalculator.getDistance(4.0, 3.0, 0.0, 0.0);
        double distance5 = DistanceCalculator.getDistance(1.0, 10.0, 1.0, 0.0);

        // Assert
        // Tomme lengder:
        assertEquals(0, distance1);
        assertEquals(0, distance2);
        assertEquals(0, distance3);

        // Negative lengder:
        assertEquals(5, distance4);
        assertEquals(10, distance5);

    }
}