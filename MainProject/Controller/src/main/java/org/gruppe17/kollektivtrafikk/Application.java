import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.gruppe17.kollektivtrafikk.db.DB_URL;
import org.gruppe17.kollektivtrafikk.db.SQLiteDatabase;
import org.gruppe17.kollektivtrafikk.repository.RouteRepository;
import org.gruppe17.kollektivtrafikk.repository.StopRepository;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.repository.UserRepository;
import org.gruppe17.kollektivtrafikk.service.RouteService;
import org.gruppe17.kollektivtrafikk.service.StopService;
import org.gruppe17.kollektivtrafikk.service.TimetableService;
import org.gruppe17.kollektivtrafikk.service.UserService;

import java.io.File;
import java.sql.Connection;



public class Application {
    public static void main(String[] args) {

        try {
            //create Javalin
            Javalin app = Javalin.create(config -> {
                config.staticFiles.add(staticFiles -> {
                    staticFiles.directory = "public";
                    staticFiles.location = Location.CLASSPATH;
                });
            });


            System.out.println("=== DATABASE DEBUG ===");
            System.out.println("DB PATH (raw) = " + DB_URL.kollektiv_DB_URL);

            File f = new File(DB_URL.kollektiv_DB_URL);
            System.out.println("ABSOLUTE PATH = " + f.getAbsolutePath());
            System.out.println("FILE EXISTS = " + f.exists());
            System.out.println("======================");
            SQLiteDatabase database = new SQLiteDatabase(DB_URL.kollektiv_DB_URL);
            Connection connection = database.startDB();

            StopRepository stopRepo = new StopRepository(connection);
            RouteRepository routeRepo = new RouteRepository(connection);
            TimetableRepository timetableRepo = new TimetableRepository(connection);


            StopService stopService = new StopService(stopRepo);
            RouteService routeService = new RouteService(routeRepo);
            TimetableService timetableService = new TimetableService(timetableRepo);

            StopController stopController = new StopController(stopService);
            RouteController routeController = new RouteController(routeService, stopService);
            TourController tourController = new TourController(timetableService);

            UserRepository userRepo = new UserRepository(connection);
            UserService userService = new UserService(userRepo);
            UserController userController = new UserController(userService);

            FrontEndController frontEndController = new FrontEndController(stopService, routeService, timetableService);
            FrontEndControllerAdmin adminFront = new FrontEndControllerAdmin(stopService, routeService, timetableService);

            app.get("/", frontEndController::serveHome); //index.html
            app.get("/admin", adminFront::serveAdminPage); //admin.html


            app.get("/api/stops", frontEndController::getAllStops);
            app.post("/search", frontEndController::searchRoute);
            app.get("/api/notification", frontEndController::getNotification);

            app.get("/api/stop2", stopController::getAllStops);
            app.post("/api/routes", routeController::addRoute);
            app.get("/api/routes", routeController::getAllRoutes);


            app.post("/admin/stops", stopController::addStop);
            app.put("/admin/stops/{id}", stopController::updateStop);
            app.delete("/admin/stops/{id}", stopController::deleteStop);

            app.post("/admin/routes", routeController::addRoute);
            app.delete("/admin/routes/{id}", routeController::deleteRoute);

            app.get("/admin/timetables", tourController::getAll);
            app.post("/admin/timetables", tourController::add);
            app.delete("/admin/timetables/{id}", tourController::delete);

            app.get("/api/users", userController::getAllUsers);
            app.get("/api/users/{id}", userController::getUserById);
            app.post("/api/users", userController::addUser);
            app.put("/api/users/{id}", userController::updateUser);
            app.delete("/api/users/{id}", userController::deleteUser);

            app.post("/login", userController::login);

            app.start(8082);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
