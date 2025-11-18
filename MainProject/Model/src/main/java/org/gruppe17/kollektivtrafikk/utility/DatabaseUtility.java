package org.gruppe17.kollektivtrafikk.utility;

import org.gruppe17.kollektivtrafikk.model.Stop;

import java.util.ArrayList;

/**
 * <p>
 * The {@code DatabaseUtility} class is a utility class meant to help with operating
 * the Repository methods when getting information from the database
 * </p>
 * Example usage:
 * <blockquote><pre>
 *     DatabaseUtility dbUtil = new DatabaseUtility();
 *     int boolean_int = dbUtil.returnIntFromBool(true);
 * </pre></blockquote>
 */

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
