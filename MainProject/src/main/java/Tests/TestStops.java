package Tests;

import Model.Route;
import Model.Stop;
import Service.RouteService;

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

        RouteService service = new RouteService();
        Route route = service.getRoute("Fredrikstad", "Ostfoldhallen");
        if (route != null) {
            System.out.println("Route: " + route.getStops().get(0).getName() + " to " +
                    route.getStops().get(1).getName() + ", Bus number: " + route.getMode());
        }


    }

}
