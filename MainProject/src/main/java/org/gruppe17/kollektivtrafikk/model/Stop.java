package org.gruppe17.kollektivtrafikk.model;

public class Stop {
    private int id;
    private String name;
    private String town;
    private double longitude;
    private double latitude;
    private boolean roof;
    private boolean accessibility;

//Represents a transport stop with a name and an identifier


    public Stop(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Stop(int id, String name, String town, double longitude, double latitude, boolean roof, boolean accessibility) {
        this.id = id;
        this.name = name;
        this.town = town;
        this.longitude = longitude;
        this.latitude = latitude;
        this.roof = roof;
        this.accessibility = accessibility;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isRoof() {
        return roof;
    }

    public void setRoof(boolean roof) {
        this.roof = roof;
    }

    public boolean isAccessibility() {
        return accessibility;
    }

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }
}
