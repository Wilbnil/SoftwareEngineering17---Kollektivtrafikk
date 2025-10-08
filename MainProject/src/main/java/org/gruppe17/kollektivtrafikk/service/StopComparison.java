package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Coordinates;
import org.gruppe17.kollektivtrafikk.model.Holdeplass;

public class StopComparison {
    public static Holdeplass finnClosest(Coordinates searchPunkt, Holdeplass[] holderplasser) {
        Holdeplass closest = null;
        double minsteAvstand = Double.MAX_VALUE;

        for (Holdeplass x : holderplasser) {
            double avstand = searchPunkt.distance(x.getPosisjon());
            if (avstand < minsteAvstand) {
                minsteAvstand = avstand;
                closest = x;
            }
        }
        return closest;
    }
}


