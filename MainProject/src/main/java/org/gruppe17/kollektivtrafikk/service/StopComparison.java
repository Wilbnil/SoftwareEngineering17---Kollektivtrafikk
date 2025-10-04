package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Coordinates;

public class StopComparison {
    public static <Holdeplass> Holdeplass finnClosest(Coordinates searchPunkt, Holdeplass[] holderplasser) {
        Holdeplass closest = null;
        double minsteAvstand = Double.MAX_VALUE;

        for (Holdeplass x : holderplasser) {
            double avstand = searchPunkt.distance(x.getX());
            if (avstand < minsteAvstand) {
                minsteAvstand = avstand;
                closest = x;
            }
        }
        return closest;
    }
}


