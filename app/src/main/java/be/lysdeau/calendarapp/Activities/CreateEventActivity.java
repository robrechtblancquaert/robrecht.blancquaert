package be.lysdeau.calendarapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Database.DataCallback;
import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

import static java.lang.Math.ceil;

public class CreateEventActivity extends AppCompatActivity {
    private EditText name;
    private EditText description;
    private EditText year;
    private Spinner month;
    private int selectedMonth;
    private EditText day;
    private CheckBox fullDay;
    private TimePicker startTime;
    private TimePicker endTime;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Intent intent = getIntent();

        this.name = findViewById(R.id.name_text);
        this.description = findViewById(R.id.description_text);
        this.year = findViewById(R.id.year_number);
        this.month = findViewById(R.id.month_text);
        this.day = findViewById(R.id.day_number);
        this.fullDay = findViewById(R.id.fullDay);
        this.startTime = findViewById(R.id.start_time);
        this.endTime = findViewById(R.id.end_time);

        // Populate month spinner with month names
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(adapter);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Add listener to full day checkbox
        final LinearLayout layout = findViewById(R.id.time_selection);
        fullDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set time selectors to 24h mode
        startTime.setIs24HourView(true);
        endTime.setIs24HourView(true);

        // Fill in the fields with default values if no id was given
        final long id = intent.getLongExtra("eventId", -1);
        if(id == -1) {
            year.setText(((Integer)Calendar.getInstance().get(Calendar.YEAR)).toString(), TextView.BufferType.EDITABLE);
            selectedMonth = (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) / 28) + 1;
            month.setSelection(selectedMonth - 1);
            day.setText(((Integer)(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % 28)).toString(), TextView.BufferType.EDITABLE);

            startTime.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            startTime.setMinute(0);

            int endHour = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 23) ? 0 : Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
            endTime.setHour(endHour);
            endTime.setMinute(0);

            // No delete option while creating new events
            Button deleteButton = findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.GONE);

        } else { // If there was an id, populate fields with data
            CalendarRepository repository = new CalendarRepository(getApplication());
            repository.getEventById(id, new DataCallback<CalendarEvent>() {
                @Override
                public void dataReceived(List<CalendarEvent> data) {
                    final CalendarEvent event = data.get(0);
                    updateView(event);
                }
            });
        }


        // Change visibility of time selection based on full day checkbox
        if(fullDay.isChecked()) {
            layout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    private void updateView(final CalendarEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText(event.getName(), TextView.BufferType.EDITABLE);
                description.setText(event.getDescription(), TextView.BufferType.EDITABLE);

                CalendarDate startDate = event.getStartDate();
                year.setText(((Integer)startDate.getYear()).toString(), TextView.BufferType.EDITABLE);
                selectedMonth = startDate.getMonth();
                month.setSelection(selectedMonth - 1);
                day.setText(((Integer)(startDate.getDay() + ((startDate.getWeek() - 1) * 7))).toString(), TextView.BufferType.EDITABLE);

                CalendarDate endDate = event.getEndDate();
                if(endDate != null) {
                    startTime.setHour(startDate.getHour());
                    startTime.setMinute(startDate.getMinute());

                    endTime.setHour(endDate.getHour());
                    endTime.setMinute(endDate.getMinute());
                } else {
                    startTime.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    startTime.setMinute(0);

                    int endHour = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 23) ? 0 : Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
                    endTime.setHour(endHour);
                    endTime.setMinute(0);
                }
            }
        });
    }

    @SuppressLint("NewApi")
    public void onSave(View v) {
        if(!checkFields()) return;
        CalendarEvent calendarEvent = new CalendarEvent();

        calendarEvent.setName(this.name.getText().toString());
        calendarEvent.setDescription(this.description.getText().toString());
        CalendarDate startDate = new CalendarDate();
        startDate.setYear(Integer.valueOf(this.year.getText().toString()));
        startDate.setMonth(selectedMonth);

        int selectedDay = Integer.valueOf(this.day.getText().toString());
        int week = (int)(ceil((double)selectedDay/7));
        int day = selectedDay - ((week - 1) * 7);

        startDate.setWeek(week);
        startDate.setDay(day);

        CalendarDate endDate = null;
        if(!fullDay.isChecked()) {
            endDate = new CalendarDate();
            endDate.setYear(Integer.valueOf(this.year.getText().toString()));
            endDate.setMonth(selectedMonth);
            endDate.setWeek(week);
            endDate.setDay(day);

            startDate.setHour(startTime.getHour());
            startDate.setMinute(startTime.getMinute());

            endDate.setHour(endTime.getHour());
            endDate.setMinute(endTime.getMinute());
        }
        calendarEvent.setStartDate(startDate);
        calendarEvent.setEndDate(endDate);

        CalendarRepository repository = new CalendarRepository(this.getApplication());
        Intent intent = getIntent();
        long id = intent.getLongExtra("eventId", -1);
        if(id == -1) {
            repository.insert(calendarEvent);
        } else {
            calendarEvent.setId(id);
            repository.update(calendarEvent);
        }

        setResult(RESULT_OK, new Intent().putExtra("eventId", id));
        finish();
    }

    public void onCancel(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onDelete(View v) {
        Intent intent = getIntent();
        long id = intent.getLongExtra("eventId", -1);
        setResult(RESULT_OK, new Intent().putExtra("eventId", id).putExtra("wasDeleted", true));

        CalendarRepository repository = new CalendarRepository(this.getApplication());
        repository.deleteById(id);

        finish();
    }

    private  boolean checkFields() {

        return true;
    }
}
