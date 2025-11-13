package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.TimetableService;


public class FrontEndControllerAdmin {

    public static void register(Javalin app,
                                StopService stopService,
                                RouteService routeService,
                                TimetableService timetableService) {
        // Serve admin page
        app.get("/admin", context -> context.redirect("admin.html"));

        RouteController.register(app, routeService, stopService);
        StopController.register(app, stopService);
        TourController.register(app, timetableService);
    }
}
