package org.gruppe17.kollektivtrafikk.service;

import java.time.*;

import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.utility.DistanceCalculator;

import java.util.ArrayList;


public class TimetableService {
    private TimetableRepository timetableRepository;

    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public ArrayList<Timetable> getAllTimetables() throws Exception {
        return timetableRepository.getAll();
    }

    public void addTimetable(Timetable timetable, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You are not an admin");
        }
        timetableRepository.insert(timetable);
    }

    public void updateTimetable(Timetable oldTimetable, Timetable newTimetable, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You are not an admin");
        }
        timetableRepository.update(oldTimetable, newTimetable);
    }

    public void deleteTimetable(Timetable timetable, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You are not an admin");
        }
        timetableRepository.delete(timetable);
    }

    // return what the time that the vehicle you want to track will arrive
    public LocalTime getSubscribedTour(int timetable_id) throws Exception {
        String today = LocalDate.now().getDayOfWeek().toString();
        LocalTime now = LocalTime.now();
        
        Timetable timetable = timetableRepository.getById(timetable_id);
        if (timetable == null) {
            return null; //no timetable found
        }
        if (!timetable.getDay_of_week().equalsIgnoreCase(today)) {
            return null; // timetable is not for today
        }
        
        LocalTime nextVehicle = timetable.getFirst_time();
        while (!nextVehicle.isAfter(timetable.getLast_time())) { 
            if (nextVehicle.isAfter(now)) {
                return nextVehicle;
            }
            nextVehicle = nextVehicle.plusMinutes(timetable.getTimeInterval());
        }

        return null; // no more routes today
    }

    // notifies the user when or if the vehicle arrives
    public void notification(int timetable_id) {
        try {
            LocalTime now = LocalTime.now();
            LocalTime yourVehicleArrival = getSubscribedTour(timetable_id);

            if (yourVehicleArrival != null) {
                Duration duration = Duration.between(now, yourVehicleArrival); 
                long minutesLeft = duration.toMinutes();

                if (minutesLeft == 1) {
                    System.out.println("Your vehicle will arrive in 1 minute!");
                }
                else if (minutesLeft > 1) {
                    System.err.println("Your vehicle arrives in " + minutesLeft + " minutes.");
                }
            }
            else {
                System.out.println("No vehicle available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // calculates the time between two stops
    public int timeBetweenStops (Route route, Stop stopA, Stop stopB) {
        try {
            // minutes from and to
            int minutesFrom = timetableRepository.getRouteStopTime(route, stopA);
            int minutesTo = timetableRepository.getRouteStopTime(route, stopB);
            
            int timeBetweenStops = (minutesTo - minutesFrom);

            return timeBetweenStops;
        
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
