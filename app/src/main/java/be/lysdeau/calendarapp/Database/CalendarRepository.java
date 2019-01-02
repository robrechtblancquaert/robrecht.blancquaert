package be.lysdeau.calendarapp.Database;

import android.app.Application;
import android.support.annotation.NonNull;

import be.lysdeau.calendarapp.Models.CalendarEvent;

public class CalendarRepository {
    private CalendarEventDao dao;

    public CalendarRepository(Application application) {
        CalendarDatabase database = CalendarDatabase.getDatabase(application);
        dao = database.calendarEventDao();
    }

    public void insert(final CalendarEvent calendarEvent) {
        Runnable runInsert = new Runnable() {
            @Override
            public void run() {
                dao.insert(calendarEvent);
            }
        };

        new Thread(runInsert).start();
    }

    public void update(final CalendarEvent calendarEvent) {
        Runnable runUpdate = new Runnable() {
            @Override
            public void run() {
                dao.update(calendarEvent);
            }
        };

        new Thread(runUpdate).start();
    }

    public void delete(final CalendarEvent calendarEvent) {
        Runnable runDelete = new Runnable() {
            @Override
            public void run() {
                dao.update(calendarEvent);
            }
        };

        new Thread(runDelete).start();
    }

    public void getAllCalendarEvents(@NonNull final DataCallback<CalendarEvent> callback) {
        Runnable runQuery = new Runnable() {
            @Override
            public void run() {
                callback.dataReceived(dao.getAllCalendarEvent());
            }
        };
    }
}
