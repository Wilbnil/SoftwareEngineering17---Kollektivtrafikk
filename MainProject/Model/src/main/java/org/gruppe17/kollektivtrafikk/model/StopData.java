package org.kollektivtrafikk.model;

import java.util.Arrays;
import java.util.List;

//must be updated once database is ready
public class StopData {
    public static List<Stop> getStops() {
        return Arrays.asList(
                new Stop(1, "Fredrikstad", "Fredrikstad", 0.0, 0.0, true, true),
                new Stop(2, "Ostfoldhallen", "Fredrikstad", 5.0, 0.0, true, true),
                new Stop(3, "Greaaker", "Sarsborg", 0.0, 3.0, true, true),
                new Stop(4, "Amfi Borg", "Sarsborg", 3.0, 3.0, true, true),
                new Stop(5, "Torsbekke", "Sarsborg", 10.0, 0.0, true, true)
        );
    }
}
