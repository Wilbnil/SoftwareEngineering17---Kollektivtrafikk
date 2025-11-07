package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.utility.AdminTemporaryStorage;

public class FrontEndControllerAdmin_OLD {

    public static void main(String[] args) {

        //Read existing routes from Json
        AdminTemporaryStorage.loadFromFile();

        // Start Javalin server for admin interface
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/public";  // folder with admin.html
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(8082);

        // Redirect to admin.html
        app.get("/", context -> context.redirect("admin.html"));

        // Endpoint: get all routes
        app.get("/routes", context -> {
            // TODO: connect to database and return all routes
            context.json("This will return list of routes");
        });

        // Endpoint: add new route
        app.post("/addRoute", context -> {
            String name = context.formParam("name");
            if (name == null || name.isBlank()) {
                name = "Deafult Route"; //defailt value
            }

            Route newRoute = new Route(0, name);
            AdminTemporaryStorage.addRoute(newRoute);
            context.status(201).result("Route temporarily added");
        });

        // Endpoint: delete route by ID
        app.delete("/deleteRoute/:id", context -> {
            // TODO: call database delete (what Snorre did)
            context.result("Delete route endpoint");
        });

        System.out.println("✅ Admin frontend running at http://localhost:8082/admin.html");
    }
}
