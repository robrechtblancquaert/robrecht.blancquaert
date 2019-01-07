package be.lysdeau.calendarapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import be.lysdeau.calendarapp.Activities.CreateEventActivity;
import be.lysdeau.calendarapp.Activities.ListEventActivity;
import be.lysdeau.calendarapp.Fragments.DateInfoFragment;
import be.lysdeau.calendarapp.Fragments.DateJumpFragment;
import be.lysdeau.calendarapp.Models.CalendarDate;

public class MainActivity extends AppCompatActivity implements DateJumpFragment.OnDateChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
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
}
