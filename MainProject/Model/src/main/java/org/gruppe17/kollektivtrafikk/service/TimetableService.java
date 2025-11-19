package org.gruppe17.kollektivtrafikk.service;

import java.time.*;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Tour;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.model.Timetable;

import java.util.ArrayList;


public class TimetableService {
    private TimetableRepository timetableRepository;
    private RouteService routeService;
    private LocalTime overrideTime = null;

    public TimetableService(TimetableRepository timetableRepository, RouteService routeService) {
        this.timetableRepository = timetableRepository;
        this.routeService = routeService;
    }

    public ArrayList<Timetable> getAllTimetables() throws Exception {
        return timetableRepository.getAll();
    }

    public Timetable getTimetableById(int routeId) throws Exception {
        return timetableRepository.getById(routeId);
    }

    public Timetable getTimetableForRoute(int routeId) throws Exception {
        String today = LocalDate.now().getDayOfWeek().toString().toLowerCase();
        Route fake = new Route(routeId, null, null, null);
        try {
            return timetableRepository.getTimetableRouteDay(fake, today);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Tour> getAllTours() {
        try {
            ArrayList<Timetable> timetables = timetableRepository.getAll();
            ArrayList<Tour> tours = new ArrayList<>();

            String today = LocalDate.now().getDayOfWeek().toString().toLowerCase();

           for (Timetable timetable : timetables) {
               if (!timetable.getDay_of_week().equalsIgnoreCase(today))
                   continue;

               Route route = routeService.getRouteById(timetable.getRoute_id());
               String type = "unknown";
                   if (route != null && route.getType() != null) {
                       type = route.getType();
                   }

               ArrayList<LocalTime> arrivals = new ArrayList<>();
               LocalTime current = timetable.getFirst_time();
               LocalTime last = timetable.getLast_time();
               if (last.isBefore(current)) last = last.plusHours(24);

               while (!current.isAfter(last)) {
                   arrivals.add(current);
                   current = current.plusMinutes(timetable.getTimeInterval());
               }
               Tour tour = new Tour(timetable.getRoute_id(), arrivals, type, LocalDate.now());
               tours.add(tour);
           }
           return tours;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

        Timetable timetable = timetableRepository.getById(timetable_id);
        if (timetable == null) return null;

        String today = LocalDate.now().getDayOfWeek().toString().toLowerCase();
        if (!timetable.getDay_of_week().equalsIgnoreCase(today)) return null;

        LocalTime nowTime = (overrideTime != null) ? overrideTime : LocalTime.now();
        LocalDate todayDate = LocalDate.now();

        LocalDateTime now = LocalDateTime.of(todayDate, nowTime);
        LocalDateTime first = LocalDateTime.of(todayDate, timetable.getFirst_time());
        LocalDateTime last  = LocalDateTime.of(todayDate, timetable.getLast_time());

        int interval = timetable.getTimeInterval();

        boolean afterMidnight = timetable.getLast_time().isBefore(timetable.getFirst_time());

        if (afterMidnight) {
            last = last.plusDays(1);
        }

        LocalDateTime next = first;

        while (!next.isAfter(last)) {

            if (!next.isBefore(now)) {
                return next.toLocalTime();
            }
            next = next.plusMinutes(interval);
        }
        return null;
    }

    public LocalTime getSubscribedTour(int timetable_id, LocalTime userTime) throws Exception {
        if (userTime != null) {
            this.overrideTime = userTime;
        } else {
            this.overrideTime = null;
        }
        return getSubscribedTour(timetable_id);
    }


    // notifies the user when or if the vehicle arrives
    public String notification(int timetable_id) {
        try {
            LocalTime arrival = getSubscribedTour(timetable_id);
            if (arrival == null) {
                return "No more buses today.";
            }

            LocalTime now = LocalTime.now();
            long minutes = java.time.Duration.between(now, arrival).toMinutes();

            if (minutes <= 0) {
                return "The bus is arriving now!";
            } else if (minutes == 1) {
                return "The bus arrives in 1 minute!";
            } else {
                return "The bus arrives in " + minutes + " minutes.";
            }

        } catch (Exception e) {
            return "Error: no timetable.";
        }
    }

    public int timeBetweenStops (Route route, Stop stopA, Stop stopB) {
        try {
            // minutes from and to
            int minutesFrom = timetableRepository.getRouteStopTime(route, stopA);
            int minutesTo = timetableRepository.getRouteStopTime(route, stopB);

            int timeBetweenStops = (minutesTo - minutesFrom);
            return timeBetweenStops > 0 ? timeBetweenStops : 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}