package org.gruppe17.kollektivtrafikk.utility;

import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

public class DatabaseUtility {
    public ArrayList<Integer> getStopIdsFromStops (ArrayList <Stop> stops) {
        ArrayList<Integer> returnList = new ArrayList<>();

        for (Stop stopX : stops) {
            returnList.add(stopX.getId());
        }

        return returnList;
    }

    public int returnIntFromBool (boolean bool){
        if (bool == true) {
            return 1;
        } else {
            return 0;
        }
    }
}
