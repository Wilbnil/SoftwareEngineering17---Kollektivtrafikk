package org.gruppe17.kollektivtrafikk.service;

import java.time.*;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;

import java.util.ArrayList;


public class TimetableService {

    private static TimetableRepository repo;

    public TimetableService(TimetableRepository repository) {
        this.repo = repository;
    }

    public static ArrayList<Timetable> getAllTimetables() throws Exception {
        return repo.getAll();
    }

    public void addTimetable(Timetable timetable) throws Exception {
        repo.insert(timetable);
    }

    public void deleteTimetable(Timetable timetable) throws Exception {
        repo.delete(timetable);
    }

    public static ArrayList<Timetable> getTimetableForRoute(Route route) throws Exception {
        return repo.getTimetablesInRoute(route);
    }

    public LocalTime getBussArrival() {
        LocalTime today;
        LocalTime now = LocalTime.now();
        
        /*
         * TODO:
         * get the bus the user wants a notification for
         * make sure its the correct day
         * return the time the bus will arrive to the users current stop
         */

        return null;
    }

    public void notification(int stop_id) {
        LocalTime now = LocalTime.now();
        LocalTime yourBussArrival = getBussArrival();

        if (yourBussArrival != null) {
            Duration duration = Duration.between(now, yourBussArrival); 
            long minutesLeft = duration.toMinutes();

            if (minutesLeft == 1) {
                System.out.println("Your bus will arrive in 1 minute!");
            }
        }
        else {
            System.out.println("No bus available.");
        }
    }

}
