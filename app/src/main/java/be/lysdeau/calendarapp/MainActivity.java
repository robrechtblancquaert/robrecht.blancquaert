package be.lysdeau.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.lysdeau.calendarapp.Activities.CreateEventActivity;
import be.lysdeau.calendarapp.Activities.ListEventActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, CreateEventActivity.class);
        Intent intent = new Intent(this, ListEventActivity.class);

        startActivity(intent);
    }
}
