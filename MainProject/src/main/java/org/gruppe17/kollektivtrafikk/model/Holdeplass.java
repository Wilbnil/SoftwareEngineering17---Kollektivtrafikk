package org.gruppe17.kollektivtrafikk.model;

public class Holdeplass {
    private String navn;
    private Coordinates posisjon;

    public Holdeplass(String navn, Coordinates posisjon) {
        this.navn = navn;
        this.posisjon = posisjon;
    }

    public String getNavn() {return navn;}
    public Coordinates getPosisjon() {return posisjon;}

}
