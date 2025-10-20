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
    @Test
    public void testDistanceCalculatorDistance_ReasonableValues() {
        //Act
        double distance = DistanceCalculator.getDistance(0.0, 0.0, 3.0, 0.0);

    }
}
