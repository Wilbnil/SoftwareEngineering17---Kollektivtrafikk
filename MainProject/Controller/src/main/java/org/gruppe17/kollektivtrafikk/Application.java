

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.gruppe17.kollektivtrafikk.*;
import org.gruppe17.kollektivtrafikk.db.SQLiteDatabase;
import org.gruppe17.kollektivtrafikk.repository.DatabaseSQLAdapter_OLD;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.service.TimetableService;


import java.sql.Connection;

public class Application {
    public static void main(String[] args) {

        try {
            // connect to database
            SQLiteDatabase db = new SQLiteDatabase();
            db.startDB();
            Connection conn = db.getConnection();
            DatabaseSQLAdapter_OLD dbAdapter = new DatabaseSQLAdapter_OLD(conn);
            //create Javalin
            Javalin app = Javalin.create(config -> {
                config.staticFiles.add(staticFiles -> {
                    staticFiles.directory = "public";
                    staticFiles.location = Location.CLASSPATH;
                });
            }).start(8082);


            //register controllers (each of the controllers has register method)
            FrontEndController.register(app, dbAdapter, conn);
            FrontEndControllerAdmin.register(app);
            RouteController.register(app);
            StopController.register(app);

            TimetableRepository timetableRepo = new TimetableRepository(conn);
            TimetableService.init(timetableRepo);
            TimetableController.register(app, timetableRepo);


            System.out.println("Server starts on port 8082");

            app.get("/favicon.ico", ctx -> {
                ctx.status(302);
            });
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Application failed to start" + e.getMessage());
        }

    }
}
