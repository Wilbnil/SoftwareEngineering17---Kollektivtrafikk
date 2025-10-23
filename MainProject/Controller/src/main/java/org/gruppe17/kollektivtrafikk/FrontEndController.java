package gruppe17.kollektivtrafikk;

import gruppe17.kollektivtrafikk.service.RouteService;
import gruppe17.kollektivtrafikk.model.Route;
import gruppe17.kollektivtrafikk.model.StopData;
import gruppe17.kollektivtrafikk.utility.DistanceCalculator;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.time.LocalDateTime;
import java.util.Map;

public class FrontEndController {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {staticFiles.directory = "/public"; staticFiles.location = Location.CLASSPATH;});
        }).start(8080);

        app.get("/", ctx -> ctx.redirect("index.html"));

        app.post("/search", ctx -> {
            try {
                String from = ctx.formParam("from");
                String to = ctx.formParam("to");

                if (from == null && to == null) {
                    ctx.status(400).result("No data");
                    return;
                }
                    RouteService service = new RouteService();
                    Route route = service.getRoute(from, to);
                    if (route == null) {
                        ctx.status(404).result("Route not found");
                        return;
                    }

                    double distance = DistanceCalculator.getDistanceFromStops(from, to);
                    LocalDateTime departure = StopData.getDepartureTime(from);
                    LocalDateTime arrival = StopData.getArrivalTime(from, to);

                    ctx.json(Map.of(
                            "route", from + " to " + to,
                            "distance", distance,
                            "departure", departure != null ? departure.toString() : "N/A",
                            "arrival", arrival != null ? arrival.toString() : "N/A",
                            "bus", route.getMode() != null ? route.getMode() : "N/A"
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                    ctx.status(500).result("Server error: " + e.getMessage());
            }
        });
    }
}
