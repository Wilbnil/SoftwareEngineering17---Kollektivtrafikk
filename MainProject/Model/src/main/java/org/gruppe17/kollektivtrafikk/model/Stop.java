package org.gruppe17.kollektivtrafikk.model;

//Represents a transport stop with a name and an identifier

/**
 * The {@code Stop} class represents station or halt along a transport route that offers passengers the ability to
 * get on or off the vehicle.
 * <p>
 * This class will be used by Routes to store which stops the route will stop at.
 * <p>
 *     </p>
 * This class will save a unique identifier in the form of a number generated in the database.
 * Additionally, a name and town is stored. A town is added so that stops with conflicting names can be separated.
 * <p>
 *     </p>
 * The coordinates of the {@code Stop} is added as doubles to store the location of stops in relation to each other.
 * This is used to find related stops that complies with filters in the MVP. This is also used to calculate the distance
 * between stops, but will need adapting into a relevant measurement of distance. Not in this MVP, but planned for a later
 * release, is the ability to chain Tours into one longer and more comprehensive Tour. The coordinates could then be used
 * for finding close stops to chain routes that are close together.
 *<p>
 *     </p>
 * The boolean values of roof and accessibility are used by the programs two filters. The first one defines whether
 * the {@code Stop} has a roof to protect the user from the elements. The accessibility defines whether the {@code Stop}
 * supports people with common disabilities, such as using a wheelchair or having impaired vision.
 *
 * <p>
 *   </p>
 * Only regular get and set methods are provided.
 * {@code StopService} will handle more logic for stops.
 */

public class Stop {

    // Variables
    private int id;
    private String name;
    private String town;
    private double latitude;
    private double longitude;
    private boolean roof;
    private boolean accessibility;


    // Constructors
    public Stop(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Stop(int id, String name, String town, double latitude, double longitude, boolean roof, boolean accessibility) {
        this.id = id;
        this.name = name;
        this.town = town;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roof = roof;
        this.accessibility = accessibility;


    }

    // Standard get and set methods.
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

    public boolean getRoof() {
        return roof;
    }

    public void setRoof(boolean roof) {
        this.roof = roof;
    }

    public boolean getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }
}
