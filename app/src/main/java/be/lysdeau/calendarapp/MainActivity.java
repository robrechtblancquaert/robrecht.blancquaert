package be.lysdeau.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import be.lysdeau.calendarapp.Activities.CreateEventActivity;
import be.lysdeau.calendarapp.Activities.ListEventActivity;

public class MainActivity extends AppCompatActivity {

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
                startActivity(intentList);
                return true;
            case R.id.action_create_event:
                Intent intentCreate = new Intent(this, CreateEventActivity.class);
                startActivity(intentCreate);
                return true;
        }
        return false;
    }
}
