public class HoldeplassSammenligner {
    public static Holdeplass finnClosest(Punkt searchPunkt, Holdeplass[] holderplasser) {
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
