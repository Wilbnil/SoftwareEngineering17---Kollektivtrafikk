package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Timetable;
import org.gruppe17.kollektivtrafikk.repository.DatabaseSQLAdapter_OLD;
import org.gruppe17.kollektivtrafikk.repository.TimetableRepository;

import java.util.ArrayList;


public class TimetableService {

    private static TimetableRepository repo;

    public static void init(TimetableRepository repository) {
        repo = repository;
    }

    public static ArrayList<Timetable> getAllTimetables() throws Exception {
        return repo.getAllTimetables();
    }

    public static void addTimetable(Timetable timetable) throws Exception {
        repo.insertTimetable(timetable);
    }

    public static void deleteTimetable(int id) throws Exception {
        repo.deleteTimetable(id);
    }

    public static Timetable getTimetableForRoute(int routeId) throws Exception {
        return repo.getTimetableForRoute(routeId);
    }

}
