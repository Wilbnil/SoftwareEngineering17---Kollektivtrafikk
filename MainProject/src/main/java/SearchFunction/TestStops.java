package SearchFunction;

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


    }

}
