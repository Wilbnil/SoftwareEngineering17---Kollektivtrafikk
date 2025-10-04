public class Punkt {
    private double x;
    private double y;

    public Punkt(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {return x;}
    public double getY() {return y;}

    public double distance(Punkt p) {
        double dx = x - p.getX();
        double dy = y - p.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}