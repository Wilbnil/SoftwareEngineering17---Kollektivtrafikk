package org.gruppe17.kollektivtrafikk;

import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.TimetableService;


public class FrontEndControllerAdmin {

    private StopService stopService;
    private RouteService routeService;
    private TimetableService timetableService;

    public FrontEndControllerAdmin(StopService stopService, RouteService routeService, TimetableService timetableService) {
        this.stopService = stopService;
        this.routeService = routeService;
        this.timetableService = timetableService;
    }

    public void requireAdmin(Context context) {
        if (!"true".equals(context.header("X-Admin"))) {
            context.status(403).result("Not authorized");
        }
    }
    public void serveAdminPage(Context context) {
        try {
            String html = new String(
                    getClass().getResourceAsStream("/public/admin.html").readAllBytes()
            );
            context.contentType("text/html").result(html);
        } catch (Exception e) {
            context.status(500).result("Error loading admin page: " + e.getMessage());
        }
    }
}
