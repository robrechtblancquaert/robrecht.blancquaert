package be.lysdeau.calendarapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Database.DataCallback;
import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

public class ListEventActivity extends AppCompatActivity {

    public static final int CREATE_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    List<CalendarEvent> displayedData;
    CalendarRepository repository;
    ListEventAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final Context context = this;

        CalendarRepository repository = new CalendarRepository(this.getApplication());
        this.repository = repository;

        repository.getAllCalendarEvents(new DataCallback<CalendarEvent>() {
            @Override
            public void dataReceived(List<CalendarEvent> data) {
                displayedData = data;

                ListEventAdapter adapter = new ListEventAdapter(displayedData, context);
                listAdapter = adapter;

                recyclerView.setAdapter(listAdapter);
            }
        });
    }

    public void onAdd(View v) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivityForResult(intent, CREATE_EVENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CREATE_EVENT_REQUEST) {
            if(resultCode == RESULT_OK) {
                Snackbar snackbar = Snackbar.make(recyclerView, R.string.event_saved, Snackbar.LENGTH_LONG);
                snackbar.show();

               repository.getLatestCreatedCalendarEvent(new DataCallback<CalendarEvent>() {
                   @Override
                   public void dataReceived(List<CalendarEvent> data) {
                       displayedData.add(data.get(0));
                       listAdapter.notifyDataSetChanged();
                   }
               });
            }
        }
        if(requestCode == EDIT_EVENT_REQUEST) {
            if(resultCode == RESULT_OK) {
                final long id = data.getLongExtra("eventId", -1);
                if(id == -1) return;
                final boolean wasDeleted = data.getBooleanExtra("wasDeleted", false);

                repository.getEventById(id, new DataCallback<CalendarEvent>() {
                    @Override
                    public void dataReceived(List<CalendarEvent> data) {
                        for(int i = 0; i < displayedData.size(); i++) {
                            if(displayedData.get(i).getId() == id) {
                                if(!wasDeleted) {
                                    displayedData.set(i, data.get(0));
                                } else {
                                    displayedData.remove(i);
                                }
                                i = displayedData.size();
                            }
                        }
                    }
                });

            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.notifyDataSetChanged();
            }
        });
    }
}
