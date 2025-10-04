public class Holdeplass {
    private String navn;
    private Punkt posisjon;

    public Holdeplass(String navn, Punkt posisjon) {
        this.navn = navn;
        this.posisjon = posisjon;
    }

    public String getNavn() {return navn;}
    public Punkt getPosisjon() {return posisjon;}

}
