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
}
