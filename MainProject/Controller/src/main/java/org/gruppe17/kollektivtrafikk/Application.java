
package org.gruppe17.kollektivtrafikk;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.gruppe17.kollektivtrafikk.db.SQLiteDatabase;
import org.gruppe17.kollektivtrafikk.repository.RepositoryRoute;
import org.gruppe17.kollektivtrafikk.repository.RepositoryStop;
import org.gruppe17.kollektivtrafikk.repository.RepositoryTimetable;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.TimetableService;

import java.sql.Connection;


public class Application {
    public static void main(String[] args) {
        try {
            //connect to database
            SQLiteDatabase database = new SQLiteDatabase();
            Connection connection = database.startDB();
            // Create repositories
            RepositoryStop stopRepo = new RepositoryStop(connection);
            RepositoryRoute routeRepo = new RepositoryRoute(connection);
            RepositoryTimetable timetableRepo = new RepositoryTimetable(connection);

            // Create services
            StopService stopService = new StopService(stopRepo);
            RouteService routeService = new RouteService(routeRepo);
            TimetableService timetableService = new TimetableService();

            //create Javalin
            Javalin app = Javalin.create(config -> {
                config.staticFiles.add(staticFiles -> {
                    staticFiles.directory = "public";
                    staticFiles.location = Location.CLASSPATH;
                });
            }).start(8082);


            //register controllers
            FrontEndController.register(app, stopService, routeService, timetableService);
            FrontEndControllerAdmin.register(app, stopService, routeService, timetableService);


            System.out.println("Server starts on port 8082");

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Application failed to start" + e.getMessage());
        }

    }
}
