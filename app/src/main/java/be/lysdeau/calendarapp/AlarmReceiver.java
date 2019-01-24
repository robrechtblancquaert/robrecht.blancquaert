package be.lysdeau.calendarapp;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.List;

import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Database.DataCallback;
import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;

import static java.lang.Math.ceil;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        final Resources res = context.getResources();

        CalendarRepository repository = new CalendarRepository((Application) context.getApplicationContext());

        Calendar calendar = Calendar.getInstance();
        CalendarDate date = new CalendarDate();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setMonth((int)ceil(calendar.get(Calendar.DAY_OF_YEAR) / (double)28));

        int selectedDay = calendar.get(Calendar.DAY_OF_YEAR) % 28;
        int week = (int)(ceil((double)selectedDay/7));
        int day = selectedDay - ((week - 1) * 7);
        // Account for leap day and year day
        if(week == 5) {
            week = 4;
            day = 8;
        }

        date.setWeek(week);
        date.setDay(day);

        repository.getEventsByDate(date, new DataCallback<CalendarEvent>() {
            @Override
            public void dataReceived(List<CalendarEvent> data) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_event)
                        .setContentTitle(res.getString(R.string.app_name))
                        .setContentText(res.getString(R.string.events_planned_notification))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(0, mBuilder.build());
            }
        });
    }
}
