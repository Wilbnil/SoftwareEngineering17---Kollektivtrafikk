package org.example;


import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.search.SearchEngine;
import org.gruppe17.kollektivtrafikk.search.SearchField;

//Test class to demonstrate the functionality of transport stop and route management.

public class TestStops {
    public static void main(String[] args) {
        Stop fredrikstad = new Stop("Fredrikstad", 1);
        Stop ostfoldhallen = new Stop("Ostfoldhallen", 2);
        Stop greaaker = new Stop("Greaaker", 3);
        Stop amfiBorg = new Stop("Amfi Borg", 4);
        Stop torsbekke = new Stop("Torsbekke", 5);

        System.out.println("Stops created: " + fredrikstad.getName() +  " - " + ostfoldhallen.getName());
        System.out.println("Stops created: " + greaaker.getName() +  " - " + amfiBorg.getName());
        System.out.println("Stops created: " + amfiBorg.getName() +  " - " + torsbekke.getName());
        System.out.println("Stops created: " + fredrikstad.getName() +  " - " + torsbekke.getName());

        SearchEngine searchEngine = new SearchEngine();
        SearchField  searchField = new SearchField();
        Route route1 = searchEngine.search(searchField.getInput());
        if (route1 != null) {
            System.out.println("Search results: From " + route1.getStops().get(0).getName() + " to " +
                    route1.getStops().get(1).getName() + ", Take bus number: " + route1.getMode());
        }

        Route route2 = searchEngine.search("Greaaker Amfi Borg");
        if (route2 != null) {
            System.out.println("Search results: From " + route2.getStops().get(0).getName() +
                    " to " + route2.getStops().get(1).getName() +
                    ", Take bus number: " + route2.getMode());
        }




    }

}
