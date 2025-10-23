package gruppe17.kollektivtrafikk.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

//must be updated once database is ready
public class StopData {
    public static List<Stop> getStops() {
        return Arrays.asList(
                new Stop(1, "Fredrikstad", "Fredrikstad", 0.0, 0.0, true, true),
                new Stop(2, "Ostfoldhallen", "Fredrikstad", 5.0, 0.0, true, true),
                new Stop(3, "Greaaker", "Sarsborg", 0.0, 3.0, true, true),
                new Stop(4, "Amfi Borg", "Sarsborg", 3.0, 3.0, true, true),
                new Stop(5, "Torsbekke", "Sarsborg", 10.0, 0.0, true, true)
        );
    }

    public static LocalDateTime getDepartureTime(String stopName) {
        return switch (stopName) {
            case "Fredrikstad" -> LocalDateTime.of(2025, 10, 19, 19, 30);
            case "Ostfoldhallen" -> LocalDateTime.of(2025, 10, 19, 19, 50);
            default -> LocalDateTime.now();
        };
    }


    // should be replaced with db connection, temporarily method for now
    public static LocalDateTime getArrivalTime(String fromName, String toName) {
        LocalDateTime departure = getDepartureTime(fromName);
        if (departure == null) {
            if ("Fredrikstad".equals(fromName) && "Ostfoldhallen".equals(toName)) {
                return departure.plusMinutes(20); // 19:30 + 20 min = 19:50
            } else if ("Greaaker".equals(fromName) && "Amfi Borg".equals(toName)) {
                return departure.plusMinutes(20); // 19:40 + 20 min = 20:00
            } else if ("Torsbekke".equals(fromName) && "Fredrikstad".equals(toName)) {
                return departure.plusMinutes(70); // 19:20 + 70 min = 20:30
            }
        } return null;
    }
}
