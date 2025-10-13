package org.gruppe17.kollektivtrafikk.model;

import java.util.Arrays;
import java.util.List;

//must be updated once database is ready
public class StopData {
    public static List<Holdeplass> getStops() {
        return Arrays.asList(
                new Holdeplass("Fredrikstad", new Coordinates(0.0, 0.0)),
                new Holdeplass("Ostfoldhallen", new Coordinates(5.0, 0.0)),
                new Holdeplass("Greaaker", new Coordinates(0.0, 3.0)),
                new Holdeplass("Amfi Borg", new Coordinates(3.0, 3.0)),
                new Holdeplass("Torsbekke", new Coordinates(10.0, 0.0))
                );
    }

}
