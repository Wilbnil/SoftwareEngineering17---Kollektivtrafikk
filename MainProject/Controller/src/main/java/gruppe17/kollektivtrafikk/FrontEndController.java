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
            config.staticFiles.add("gruppe17.kollektivtrafikk/View", Location.EXTERNAL);
        }).start(8080);

        app.get("/", ctx -> ctx.redirect("/View/index.html"));

        app.post("/search", ctx -> {
            String from = ctx.formParam("from");
            String to = ctx.formParam("to");
            if (from == null || to == null) {
                RouteService service = new RouteService();
                Route route = service.getRoute(from, to);
                if (route != null) {
                    double distance = DistanceCalculator.getDistanceFromStops(from, to);
                    LocalDateTime departure = StopData.getDepartureTime(from);
                    LocalDateTime arrival = StopData.getArrivalTime(from, to);

                    ctx.json(Map.of(
                            "route", from + " to " + to,
                            "distance", distance,
                            "departure", departure.toString(),
                            "arrival", arrival.toString(),
                            "bus", route.getMode()
                    ));
                } else {
                    ctx.status(404).result("Not Found");
                }
            } else {
                ctx.status(400).result("No data");
            }
        });
    }
}
