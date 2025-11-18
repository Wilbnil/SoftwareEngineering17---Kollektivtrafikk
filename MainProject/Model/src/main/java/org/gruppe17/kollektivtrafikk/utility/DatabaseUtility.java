package org.gruppe17.kollektivtrafikk.utility;

import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

public class DatabaseUtility {

    /**
     * Returns an ArrayList of Stop ids
     *
     * @param {ArrayList<Stop>} stops - ArrayList of Stops
     * @return {ArrayList<Integer>} - ArrayList of Stop ids
     */
    public ArrayList<Integer> getStopIdsFromStops (ArrayList <Stop> stops) {
        ArrayList<Integer> returnList = new ArrayList<>();

        for (Stop stopX : stops) {
            returnList.add(stopX.getId());
        }

        return returnList;
    }

    /**
     * Returns an int from a boolean
     *
     * @param {boolean} bool - input bool
     * @return {int} - 1 or 0
     */
    public int returnIntFromBool (boolean bool){
        if (bool == true) {
            return 1;
        } else {
            return 0;
        }
    }
}
