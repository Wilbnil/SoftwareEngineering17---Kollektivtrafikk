package org.example;


import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.search.SearchEngine;
import org.gruppe17.kollektivtrafikk.search.SearchField;

//Test class to demonstrate the functionality of transport stop and route management.

public class TestStops {
    public static void main(String[] args) {
        Stop fredrikstad = new Stop(1, "Fredrikstad");
        Stop ostfoldhallen = new Stop(2, "Ostfoldhallen");
        Stop greaaker = new Stop(3, "Greaaker");
        Stop amfiBorg = new Stop(4, "Amfi Borg");
        Stop torsbekke = new Stop(5, "Torsbekke");

        System.out.println("Stops created: " + fredrikstad.getName() +  " - " + ostfoldhallen.getName());
        System.out.println("Stops created: " + greaaker.getName() +  " - " + amfiBorg.getName());
        System.out.println("Stops created: " + amfiBorg.getName() +  " - " + torsbekke.getName());
        System.out.println("Stops created: " + fredrikstad.getName() +  " - " + torsbekke.getName());

        SearchEngine searchEngine = new SearchEngine();
        SearchField  searchField = new SearchField();
        Route route1 = searchEngine.search(searchField.getInput());


        Route route2 = searchEngine.search("Greaaker Amfi Borg");





    }

}

