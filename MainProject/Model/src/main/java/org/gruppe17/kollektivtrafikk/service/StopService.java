package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.Stop;
import org.gruppe17.kollektivtrafikk.repository.DatabaseAdminSQLAdapter_OLD;
import org.gruppe17.kollektivtrafikk.repository.DatabaseSQLAdapter_OLD;
import org.gruppe17.kollektivtrafikk.repository.StopRepository;

import java.util.List;

public class StopService {


    public static List<Stop> getAllStops() { return DatabaseSQLAdapter_OLD.getAllStopsFromDatabase(); }
    public static Stop getStopById(int id) { return DatabaseSQLAdapter_OLD.getStopFromDatabaseWhere(id); }
    public static void addStop(Stop stop) throws Exception {  DatabaseAdminSQLAdapter_OLD.insertStopIntoDatabase(stop); }
    public static void updateStop(Stop stop, Stop newStop) throws Exception { DatabaseAdminSQLAdapter_OLD.updateStopInDatabase(stop, newStop); }
    public static void deleteStop(Stop stop) throws Exception { DatabaseAdminSQLAdapter_OLD.deleteStopInDatabase(stop); }
}

