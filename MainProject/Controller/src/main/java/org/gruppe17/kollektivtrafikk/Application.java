package org.gruppe17.kollektivtrafikk;

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
import java.sql.Connection;

/**
 * The {@code Application} class serves as the entry point of the entire Kollektivtrafikk project.
 * It is responsible for:
 *
 * <li>Starting Javalin web server</li>
 * <li>Serving static frontend files (HTML)</li>
 * <li>Initializing database connection</li>
 * <li>Contructing repositories, services, adn controllers</li>
 * <li>Registering all REST API endpoints</li>
 *
 * <p>This class follows a simplified MVC architecture, where:</p>
 * <li>Repositories: handles database access</li>
 * <li>Services: contain all business logic</li>
 * <li>Controllers: respond to HTTP requests</li>
 *
 * Correct Usage Example:
 * <blockquote><pre>
 *     java Application
 * </pre></blockquote>
 *
 * Incorrect Usage Example:
 * <blockquote><pre>
 *     new StopController(null);
 * </pre></blockquote>
 *
 * <p>Port: the web server runs on port {@code 8082}.</p>
 *
 */

public class Application {
    public static void main(String[] args) {
        try {
            //create Javalin
            Javalin app = Javalin.create(config -> {
                config.staticFiles.add("C:\\Users\\snorr\\Documents\\GitHub\\Software Engineer\\SoftwareEngineering17---Kollektivtrafikk\\MainProject\\View\\src\\main\\resources\\public", Location.EXTERNAL);
            }).start(7000);

            //Javalin app = Javalin.create().start(7000);

            SQLiteDatabase database = new SQLiteDatabase(DB_URL.kollektiv_DB_URL);
            Connection connection = database.startDB();

            StopRepository stopRepo = new StopRepository(connection);
            RouteRepository routeRepo = new RouteRepository(connection);
            TimetableRepository timetableRepo = new TimetableRepository(connection);
            UserRepository userRepo = new UserRepository(connection);

            StopService stopService = new StopService(stopRepo);
            RouteService routeService = new RouteService(routeRepo, timetableRepo);
            TimetableService timetableService = new TimetableService(timetableRepo, routeService);
            UserService userService = new UserService(userRepo);

            StopController stopController = new StopController(stopService);
            RouteController routeController = new RouteController(routeService, stopService, timetableService);
            TourController tourController = new TourController(timetableService);
            UserController userController = new UserController(userService);


            app.get("/", context -> context.redirect("index.html")); //index.html
            app.get("/admin", context -> context.redirect("admin.html"));

            //Stop
            app.get("/api/stops", stopController::getAllStops);
            app.post("/admin/stops", stopController::addStop);
            app.put("/admin/stops/{id}", stopController::updateStop);
            app.delete("/admin/stops/{id}", stopController::deleteStop);

            // Route
            app.get("/api/routes", routeController::getAllRoutes);
            app.get("/search", routeController::searchRoute);
            app.get("/api/routes/stops", routeController::getStopsInRoute);
            app.post("/admin/routes", routeController::addRoute);
            app.put("/admin/routes", routeController::updateRoute);
            app.delete("/admin/routes/{id}", routeController::deleteRoute);

            // Notification
            app.get("/api/notification", tourController::getNotification);

            // Timetables
            app.get("/admin/timetables", tourController::getAll);
            app.post("/admin/timetables", tourController::add);
            app.put("/admin/timetables/{id}", tourController::update);
            app.delete("/admin/timetables/{id}", tourController::delete);

            // Users
            app.get("/api/users", userController::getAllUsers);
            app.get("/api/users/{id}", userController::getUserById);
            app.post("/api/users", userController::addUser);
            app.put("/api/users/{id}", userController::updateUser);
            app.delete("/api/users/{id}", userController::deleteUser);
            app.post("/login", userController::login);


            //app.start(8082);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
