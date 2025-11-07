package model;

import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator_OLD;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceCalculatorTest {

    // Realistiske verdier.
    @Test
    public void testDistanceCalculatorTravelTime_ReasonableValues() {
        // Arrange
        LocalDateTime timeFrom1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30));
        LocalDateTime timeTo1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0));

        LocalDateTime timeFrom2 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime timeTo2 = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 22));

        LocalDateTime timeFrom3 = LocalDateTime.of
                (LocalDate.of(2025,11, 3), LocalTime.of(23, 50));
        LocalDateTime timeTo3 = LocalDateTime.of
                (LocalDate.of(2025, 11, 4), LocalTime.of(0, 30));
        // Act
        LocalTime time1 = DistanceCalculator_OLD.calculateTravelTime(timeFrom1, timeTo1);
        LocalTime time2 = DistanceCalculator_OLD.calculateTravelTime(timeFrom2, timeTo2);
        LocalTime time3 = DistanceCalculator_OLD.calculateTravelTime(timeFrom3, timeTo3);

        // Assert
        assertEquals(LocalTime.of(0, 30), time1);
        assertEquals(LocalTime.of(6, 22), time2);
        assertEquals(LocalTime.of(0, 40), time3);

        // Test av overloadet metode som kun tar inn tid:
        // Assert
        LocalTime timeOnlyFrom1 = LocalTime.of(17, 30);
        LocalTime timeOnlyTo1 = LocalTime.of(17, 31);

        LocalTime timeOnlyFrom2 = LocalTime.of(10, 25);
        LocalTime timeOnlyTo2 = LocalTime.of(11, 5);

        LocalTime timeOnlyFrom3 = LocalTime.of(0, 0);
        LocalTime timeOnlyTo3 = LocalTime.of(23, 59);

        // Act
        LocalTime timeOnly1 = DistanceCalculator_OLD.calculateTravelTime(timeOnlyFrom1, timeOnlyTo1);
        LocalTime timeOnly2 = DistanceCalculator_OLD.calculateTravelTime(timeOnlyFrom2, timeOnlyTo2);
        LocalTime timeOnly3 = DistanceCalculator_OLD.calculateTravelTime(timeOnlyFrom3, timeOnlyTo3);

        // Assert
        assertEquals(LocalTime.of(0, 1), timeOnly1);
        assertEquals(LocalTime.of(0, 40), timeOnly2);
        assertEquals(LocalTime.of(23, 59), timeOnly3);
    }

    // Uforventede verdier
    @Test
    public void testDistanceCalculatorTravelTime_UnexpectedValues() {

        // Reverserte tider:
        // Arrange
        LocalDateTime timeFrom1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30));
        LocalDateTime timeTo1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0));

        LocalDateTime timeFrom2 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime timeTo2 = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 22));

        LocalDateTime timeFrom3 = LocalDateTime.of
                (LocalDate.of(2025,11, 3), LocalTime.of(23, 50));
        LocalDateTime timeTo3 = LocalDateTime.of
                (LocalDate.of(2025, 11, 4), LocalTime.of(0, 30));
        // Act
        LocalTime time1 = DistanceCalculator_OLD.calculateTravelTime(timeTo1, timeFrom1);
        LocalTime time2 = DistanceCalculator_OLD.calculateTravelTime(timeTo2, timeFrom2);
        LocalTime time3 = DistanceCalculator_OLD.calculateTravelTime(timeTo3, timeFrom3);

        // Assert
        assertEquals(LocalTime.of(23, 30), time1);
        assertEquals(LocalTime.of(17, 38), time2);
        assertEquals(LocalTime.of(23, 20), time3);

        // Lange og korte tider:
        // Arrange
        LocalDateTime timeFrom4 = LocalDateTime.of
                (LocalDate.of(2000, 1, 1), LocalTime.of(12, 0));
        LocalDateTime timeTo4 = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));

        LocalDateTime timeFrom5 = LocalDateTime.of
                (LocalDate.of(1970,1, 1), LocalTime.of(0, 0));
        LocalDateTime timeTo5 = LocalDateTime.of
                (LocalDate.of(2070, 1, 11), LocalTime.of(0, 0));

        LocalDateTime timeFrom6 = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        LocalDateTime timeTo6 = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59));

        LocalDateTime timeFrom7 = LocalDateTime.of(LocalDate.now(), LocalTime.of(7,0));
        LocalDateTime timeTo7 = LocalDateTime.of(LocalDate.now(), LocalTime.of(7,0));

        // Act
        LocalTime time4 = DistanceCalculator_OLD.calculateTravelTime(timeFrom4, timeTo4);
        LocalTime time4Reversed = DistanceCalculator_OLD.calculateTravelTime(timeTo4, timeFrom4);
        LocalTime time5 = DistanceCalculator_OLD.calculateTravelTime(timeFrom5, timeTo5);
        LocalTime time6 = DistanceCalculator_OLD.calculateTravelTime(timeFrom6, timeTo6);
        LocalTime time6Reversed = DistanceCalculator_OLD.calculateTravelTime(timeTo6, timeFrom6);
        LocalTime time7 = DistanceCalculator_OLD.calculateTravelTime(timeFrom7, timeTo7);

        // Assert
        assertEquals(LocalTime.of(0, 0), time4);
        assertEquals(LocalTime.of(0, 0), time4Reversed);
        assertEquals(LocalTime.of(0, 0), time5);
        assertEquals(LocalTime.of(23, 59), time6);
        assertEquals(LocalTime.of(0, 1), time6Reversed);
        assertEquals(LocalTime.of(0, 0), time7);

    }

    // Opplagte testverdier:
    @Test
    public void testDistanceCalculatorDistance_ReasonableValues() {
        //Act
        double distance = DistanceCalculator_OLD.getDistance(0.0, 0.0, 3.0, 0.0);

        // Assert
        assertEquals(3, distance);
    }

    // Mindre forventede verdier:
    @Test
    public void testDistanceCalculatorDistance_UnexpectedValues() {
        //Act
        double distance1 = DistanceCalculator_OLD.getDistance(2.0, 0.0, 2.0, 0.0);
        double distance2 = DistanceCalculator_OLD.getDistance(0.0, 0.0, 0.0, 0.0);
        double distance3 = DistanceCalculator_OLD.getDistance(0.1, 0.1, 0.1, 0.1);

        double distance4 = DistanceCalculator_OLD.getDistance(4.0, 3.0, 0.0, 0.0);
        double distance5 = DistanceCalculator_OLD.getDistance(1.0, 10.0, 1.0, 0.0);

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
