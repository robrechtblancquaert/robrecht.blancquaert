package be.lysdeau.calendarapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import be.lysdeau.calendarapp.Models.CalendarEvent;

@Database(entities = {CalendarEvent.class}, version = 1)
public abstract class CalendarDatabase extends RoomDatabase {

    public abstract CalendarEventDao calendarEventDao();

    static CalendarDatabase getDatabase(final Context context)
    {
        return Room.databaseBuilder(context.getApplicationContext(),
                CalendarDatabase.class,
                "calendar_database").build();
    }
}
