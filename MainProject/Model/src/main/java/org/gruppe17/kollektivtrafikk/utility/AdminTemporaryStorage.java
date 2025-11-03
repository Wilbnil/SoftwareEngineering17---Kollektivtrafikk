package org.gruppe17.kollektivtrafikk.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.gruppe17.kollektivtrafikk.model.Route;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class AdminTemporaryStorage {
    private static List<Route> routes = new ArrayList<>();
    private static final String FILE_PATH = "routes.json";

    public static void addRoute(Route route) {
        routes.add(route);
        saveToFile();
    }

    public static List<Route> getRoutes() {
        return routes;
    }

    //save routes to Json
    private static void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            new Gson().toJson(routes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //read routes from Json  when server starts
    public static void loadFromFile() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            routes = new Gson().fromJson(reader, new TypeToken<List<Route>>(){}.getType());
            if (routes == null) routes = new ArrayList<>();
        } catch (FileNotFoundException e) {
            routes = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
