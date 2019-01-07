package be.lysdeau.calendarapp.Database;

import android.app.Application;
import android.support.annotation.NonNull;

import be.lysdeau.calendarapp.Models.CalendarDate;
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
        new Thread(runQuery).start();
    }

    public void getLatestCreatedCalendarEvent(@NonNull final DataCallback<CalendarEvent> callback) {
        Runnable runQuery = new Runnable() {
            @Override
            public void run() {
                callback.dataReceived(dao.getLatestCreatedEvent());
            }
        };
        new Thread(runQuery).start();
    }

    public void getEventById(final long eventId, @NonNull final DataCallback<CalendarEvent> callback) {
        Runnable runQuery = new Runnable() {
            @Override
            public void run() {
                callback.dataReceived(dao.getEventById(eventId));
            }
        };
        new Thread(runQuery).start();
    }

    public void getEventsByDate(final CalendarDate date, final DataCallback<CalendarEvent> callback) {
        Runnable runQuery = new Runnable() {
            @Override
            public void run() {
                callback.dataReceived(dao.getEventsByDate(date.getYear(), date.getMonth(), date.getWeek(), date.getDay()));
            }
        };
        new Thread(runQuery).start();
    }

    public void deleteById(final long eventId) {
        Runnable runDelete = new Runnable() {
            @Override
            public void run() {
                dao.deleteById(eventId);
            }
        };
        new Thread(runDelete).start();
    }
}
