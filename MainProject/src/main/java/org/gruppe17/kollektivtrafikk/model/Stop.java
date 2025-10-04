package org.gruppe17.kollektivtrafikk.model;

public class Stop {
    private String name;
    private int id;

//Represents a transport stop with a name and an identifier

    public Stop(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Stop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
