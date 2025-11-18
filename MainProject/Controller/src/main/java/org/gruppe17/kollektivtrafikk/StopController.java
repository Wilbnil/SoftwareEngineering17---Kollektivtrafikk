package org.gruppe17.kollektivtrafikk;


import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.service.StopService;


import java.util.ArrayList;

public class StopController {

    private StopService stopService;


    public StopController(StopService stopService) {
        this.stopService = stopService;
    }



      public void getAllStops(Context context) {
        ArrayList<Stop> stops = stopService.getAllStops();
        context.json(stops);
      }

       public void addStop(Context context) {
            try {

                String name = context.formParam("name");
                String town = context.formParam("town");
                float lat = Float.parseFloat(context.formParam("lat"));
                float lon = Float.parseFloat(context.formParam("lon"));
                boolean roof = Boolean.parseBoolean(context.formParam("roof"));
                boolean accessibility = Boolean.parseBoolean(context.formParam("accessibility"));

                Stop stop = new Stop(0, name, town, lat, lon, roof, accessibility);
                stopService.addStop(stop, true);

                context.status(201).result("Stop added");

            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Error adding stop" + e.getMessage());
            }
        }

        public void updateStop(Context context) {
            try {

                int id = Integer.parseInt(context.pathParam("id"));
                Stop oldStop = stopService.getStopById(id);

                if (oldStop == null) {
                    context.status(404).result("Stop not found");
                    return;
                }

                String name = context.formParam("name");
                String town = context.formParam("town");
                float lat = Float.parseFloat(context.formParam("lat"));
                float lon = Float.parseFloat(context.formParam("lon"));
                boolean roof = Boolean.parseBoolean(context.formParam("roof"));
                boolean accessibility = Boolean.parseBoolean(context.formParam("accessibility"));

                Stop updated = new Stop(id, name, town, lat, lon, roof, accessibility);
                stopService.updateStop(oldStop, updated, true);

                context.status(200).result("Stop updated");
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Could not update stop" + e.getMessage());
            }
        }

        public void deleteStop(Context context) {
            try {

                int id = Integer.parseInt(context.pathParam("id"));
                Stop stop = stopService.getStopById(id);

                if (stop == null) {
                    context.status(404).result("Stop not found");
                    return;
                }
                stopService.deleteStop(stop, true);
                context.status(204).result("Stop deleted");

            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Could not delete stop" + e.getMessage());
            }
        }

}

