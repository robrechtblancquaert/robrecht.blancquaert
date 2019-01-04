package be.lysdeau.calendarapp.Activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

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

    // Only need to support after 16, which is correct
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        this.name = findViewById(R.id.name_text);
        this.description = findViewById(R.id.description_text);
        this.year = findViewById(R.id.year_number);
        this.month = findViewById(R.id.month_text);
        this.day = findViewById(R.id.day_number);
        this.fullDay = findViewById(R.id.fullDay);
        this.startTime = findViewById(R.id.start_time);
        this.endTime = findViewById(R.id.end_time);

        year.setText(((Integer)Calendar.getInstance().get(Calendar.YEAR)).toString(), TextView.BufferType.EDITABLE);

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
        selectedMonth = (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) / 28) + 1;
        month.setSelection(selectedMonth - 1);

        day.setText(((Integer)(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % 28)).toString(), TextView.BufferType.EDITABLE);

        final LinearLayout layout = findViewById(R.id.time_selection);
        if(fullDay.isChecked()) {
            layout.setVisibility(View.GONE);
        }
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

        startTime.setIs24HourView(true);
        endTime.setIs24HourView(true);

        startTime.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        startTime.setMinute(0);

        int endHour = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 23) ? 0 : Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;

        endTime.setHour(endHour);
        endTime.setMinute(0);
    }

    // Only need to support after 16, which is correct
    @SuppressLint("NewApi")
    public void onSave(View v) {
        CalendarEvent calendarEvent = new CalendarEvent();

        calendarEvent.setName(this.name.getText().toString());
        calendarEvent.setDescription(this.description.getText().toString());
        CalendarDate startDate = new CalendarDate();
        startDate.setYear(Integer.valueOf(this.year.getText().toString()));
        startDate.setMonth(selectedMonth);
        int selectedDay = Integer.valueOf(this.day.getText().toString());
        startDate.setWeek((selectedDay/7) + 1);
        startDate.setDay((selectedDay%7) + 1);

        CalendarDate endDate = null;
        if(!fullDay.isChecked()) {
            endDate = new CalendarDate();
            endDate.setYear(Integer.valueOf(this.year.getText().toString()));
            endDate.setMonth(selectedMonth);
            endDate.setWeek((selectedDay/7) + 1);
            endDate.setDay((selectedDay%7) + 1);

            startDate.setHour(startTime.getHour());
            startDate.setMinute(startTime.getMinute());

            endDate.setHour(endTime.getHour());
            endDate.setMinute(endTime.getMinute());
        }
        calendarEvent.setStartDate(startDate);
        calendarEvent.setEndDate(endDate);

        //TODO: add error checking

        CalendarRepository repository = new CalendarRepository(this.getApplication());
        repository.insert(calendarEvent);
    }

    public void onCancel(View v) {

    }

    public void onDelete(View v) {

    }
}
