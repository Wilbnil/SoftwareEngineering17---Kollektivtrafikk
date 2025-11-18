package org.gruppe17.kollektivtrafikk.utility;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DatabaseUtilityTest {

    @Test
    @DisplayName("Ids returned successfully")
    public void getStopIdsFromStops_IdsReturnedSuccessfully() {
        //Arrange
        DatabaseUtility dbUtil = new DatabaseUtility();

        ArrayList<Integer> stopIds;

        ArrayList<Stop> stops = new ArrayList<>();

        stops.add(new Stop(10, "Fredrikstad stasjon", "Fredrikstad",59.2089, 10.9506, false, false));
        stops.add(new Stop(15, "Oslo S", "Oslo", 59.9111, 10.7535, false, false));
        stops.add(new Stop(17, "New cool stop", "Newland", 10, 10, true, false));
        stops.add(new Stop(9, "Sarpsborg stasjon", "Sarpsborg", 59.2861, 11.118, false, false));

        //Act
        stopIds = dbUtil.getStopIdsFromStops(stops);

        //Assert
        Assertions.assertEquals(4, stopIds.size());
        Assertions.assertEquals(10, stopIds.getFirst());
        Assertions.assertEquals(9, stopIds.getLast());
    }

    @Test
    @DisplayName("Int is returned with the correct value")
    public void returnIntFromBool_IntReturnedSuccessfully() {
        //Arrange
        DatabaseUtility dbUtil = new DatabaseUtility();

        boolean bool_false = false;
        boolean bool_true = true;

        int int_false;
        int int_true;

        //Act
        int_false = dbUtil.returnIntFromBool(bool_false);
        int_true = dbUtil.returnIntFromBool(bool_true);

        //Assert
        Assertions.assertEquals(0, int_false);
        Assertions.assertEquals(1, int_true);
    }
}
