package be.lysdeau.calendarapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.mortbay.resource.Resource;

import java.util.Calendar;
import java.util.List;

import be.lysdeau.calendarapp.Activities.CreateEventActivity;
import be.lysdeau.calendarapp.Activities.ListEventActivity;
import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Database.DataCallback;
import be.lysdeau.calendarapp.Fragments.DateInfoFragment;
import be.lysdeau.calendarapp.Fragments.DateJumpFragment;
import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;

import static java.lang.Math.ceil;

public class MainActivity extends AppCompatActivity implements DateJumpFragment.OnDateChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        setNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_list_events:
                Intent intentList = new Intent(this, ListEventActivity.class);
                startActivityForResult(intentList, 0);
                return true;
            case R.id.action_create_event:
                Intent intentCreate = new Intent(this, CreateEventActivity.class);
                startActivityForResult(intentCreate, 0);
                return true;
        }
        return false;
    }

    @Override
    public void onDateChanged(CalendarDate calendarDate) {
        DateInfoFragment dateInfo = (DateInfoFragment) getSupportFragmentManager().findFragmentById(R.id.frg_info);
        dateInfo.setDate(calendarDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        findViewById(R.id.button_go).callOnClick();
    }

    // Source: https://stackoverflow.com/questions/23440251/how-to-repeat-notification-daily-on-specific-time-in-android-through-background
    private void setNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
