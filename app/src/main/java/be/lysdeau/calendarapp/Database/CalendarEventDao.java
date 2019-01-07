package be.lysdeau.calendarapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import be.lysdeau.calendarapp.Models.CalendarEvent;

@Dao
public interface CalendarEventDao {

    @Insert
    long insert(CalendarEvent calendarEvent);

    @Update
    int update(CalendarEvent calendarEvent);

    @Delete
    int delete(CalendarEvent calendarEvent);

    @Query("SELECT * FROM events")
    List<CalendarEvent> getAllCalendarEvent();

    @Query("SELECT * FROM events ORDER BY id DESC LIMIT 1")
    List<CalendarEvent> getLatestCreatedEvent();

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    List<CalendarEvent> getEventById(long eventId);

    @Query("SELECT * FROM events WHERE start_year = :year AND start_month = :month AND start_week = :week AND start_day = :day")
    List<CalendarEvent> getEventsByDate(int year, int month, int week, int day);

    @Query("DELETE FROM events WHERE id = :eventId")
    void deleteById(long eventId);
}
