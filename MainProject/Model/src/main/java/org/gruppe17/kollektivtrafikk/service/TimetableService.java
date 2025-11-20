package org.gruppe17.kollektivtrafikk.service;

import java.time.*;

import org.gruppe17.kollektivtrafikk.model.Route;
import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.model.Tour;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;
import org.gruppe17.kollektivtrafikk.model.Timetable;

import java.util.ArrayList;


/**
 * The {@code TimetableService} class handles all the logic related stuff for the bus stops in the public transport system.
 *
 * <p>
 * This service class provides methods like retrieving, adding, updating and deleting timetables.
 * It includes as well, methods that generate tours based on timetables, tracking arrivals and
 * calculating time between stops.
 * This class interacts with {@code TimetableRepository} and {@code RouteService} in order to do its database operations.
 * </p>
 *
 * <p>
 * Only admins are able to do certain stuff like adding, updating and deleting timetables.
 * </p>
 *
 * <p>
 * Example usage:
 * <blockquote><pre>
 * ArrayList<Tour> tours = timetableService.getAllTours();
 * LocalTime nextArrival = timetableService.getSubscribedTour(6);
 * String notification = timetableService.notification(7);
 * </pre></blockquote>
 * </p>
 *
 * <p>
 * {@code TimetableService} should be instantiated with {@code TimetableRepository} and {@code RouteService}.
 * </p>
 */
public class TimetableService {
    private TimetableRepository timetableRepository;
    private RouteService routeService;
    private LocalTime overrideTime = null;

    /**
     * Creates a new TimetableService with the repositories and services.
     *
     * @param timetableRepository The repository used
     * @param routeService The service used
     */
    public TimetableService(TimetableRepository timetableRepository, RouteService routeService) {
        this.timetableRepository = timetableRepository;
        this.routeService = routeService;
    }

    /**
     * Gets all the timetables from the database.
     *
     * @return An ArrayList with all the timetables
     * @throws Exception if an error happens
     */
    public ArrayList<Timetable> getAllTimetables() throws Exception {
        return timetableRepository.getAll();
    }

    /**
     * Gets a timetable by its id.
     *
     * @param routeId The identifier of the timetable
     * @return The timetable if found
     * @throws Exception if an error happens
     */
    public Timetable getTimetableById(int routeId) throws Exception {
        return timetableRepository.getById(routeId);
    }

    /**
     * Gets the timetable for a route on the current day.
     *
     * @param routeId The identifier of the route
     * @return The timetable for today, null if not found
     * @throws Exception if an error happens
     */
    public Timetable getTimetableForRoute(int routeId) throws Exception {
        String today = LocalDate.now().getDayOfWeek().toString().toLowerCase();
        Route fake = new Route(routeId, null, null, null);
        try {
            return timetableRepository.getTimetableRouteDay(fake, today);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets all tours for today.
     * <p>
     * This method makes Tour objects out from calculating all the departure times in each timetable.
     * It uses the first departure, last departure and the time interval between them.
     * Only the timetables that matches the current day of the week is allowed.
     * </p>
     *
     * @return An arrayList with all the tours for today, otherwise an empty Arraylist
     */
    public ArrayList<Tour> getAllTours() {
        try {
            ArrayList<Timetable> timetables = timetableRepository.getAll();
            ArrayList<Tour> tours = new ArrayList<>();

            String today = LocalDate.now().getDayOfWeek().toString().toLowerCase();

           for (Timetable timetable : timetables) {
               if (!timetable.getDay_of_week().equalsIgnoreCase(today))
                   continue;
                /*

               Not needed when type is moved to Route class.
               Route route = routeService.getRouteById(timetable.getRoute_id());
               String type = "unknown";
                   if (route != null && route.getType() != null) {
                       type = route.getType();
                   }
                */

               ArrayList<LocalTime> arrivals = new ArrayList<>();
               LocalTime current = timetable.getFirst_time();
               LocalTime last = timetable.getLast_time();
               if (last.isBefore(current)) last = last.plusHours(24);

               while (!current.isAfter(last)) {
                   arrivals.add(current);
                   current = current.plusMinutes(timetable.getTimeInterval());
               }
               Tour tour = new Tour(timetable.getRoute_id(), arrivals, LocalDate.now());
               tours.add(tour);
           }
           return tours;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new timetable to the database.
     *
     * @param timetable The Stop that is going to be added
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void addTimetable(Timetable timetable, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You are not an admin");
        }
        timetableRepository.insert(timetable);
    }

    /**
     * Updates a timetable in the database.
     *
     * @param oldTimetable The old timetable that is going to be updated
     * @param newTimetable The new timetable that gets its new updated information
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void updateTimetable(Timetable oldTimetable, Timetable newTimetable, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You are not an admin");
        }
        timetableRepository.update(oldTimetable, newTimetable);
    }

    /**
     * Deletes a timetable from the database.
     *
     * @param timetable The timetable that is going to be deleted
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void deleteTimetable(Timetable timetable, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You are not an admin");
        }
        timetableRepository.delete(timetable);
    }

    /**
     * Returns the next arrival time for a route you've subscribed to, based on the current time.
     *
     * <p>
     * This method finds the next vehicle arrival time for a given timetable you've subscribed to,
     * based on your current time. It checks if the timetable is valid for today's weekday and then
     * finds the next departure after the current time.
     * </p>
     *
     * @param timetable_id The identifier of the timetable
     * @return The next arrival time as LocalTime, otherwise null if there are no more departures today
     * @throws Exception if an error happens
     */
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

    /**
     * Returns the coming arrival time for a subscribed tour based on a set time.
     * <p>
     * This method allows testing with a custom time compared to current time.
     * </p>
     *
     * @param timetable_id The identifier of the timetable
     * @param userTime The custom time, otherwise null is current time
     * @return The next arrival time, otherwise null if there doesn't exist any more departures
     * @throws Exception if an error happens
     */
    public LocalTime getSubscribedTour(int timetable_id, LocalTime userTime) throws Exception {
        if (userTime != null) {
            this.overrideTime = userTime;
        } else {
            this.overrideTime = null;
        }
        return getSubscribedTour(timetable_id);
    }

    /**
     * Makes a notification message on your next vehicle arrival.
     * <p>
     * This method calculates the time needed for your next arrival and returns
     * messages out from how far/long until it arrives.
     * </p>
     *
     * @param timetable_id The identifier of the timetable
     * @return Notification message about the vehicles arrival time
     */
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

    /**
     * Calculates the time in minutes between two stops in a route.
     * <p>
     * This method gets the planned time from the route's starting point to a stop and
     * calculates the time.
     * </p>
     *
     * @param route The route that has the two stops
     * @param stopA The departure stop
     * @param stopB The arrival stop
     * @return The travel in minutes between the stops, otherwise 0 if an error happens.
     */
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