package be.lysdeau.calendarapp.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Database.DataCallback;
import be.lysdeau.calendarapp.MainActivity;
import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

import static java.lang.Math.ceil;

public class CreateEventActivity extends AppCompatActivity {
    private EditText name;
    private EditText description;
    private NumberPicker year;
    private NumberPicker month;
    private NumberPicker day;
    private CheckBox fullDay;
    private TimePicker startTime;
    private TimePicker endTime;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.create_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();

        this.name = findViewById(R.id.name_text);
        this.description = findViewById(R.id.description_text);
        this.year = findViewById(R.id.year_number);
        this.month = findViewById(R.id.month_text);
        this.day = findViewById(R.id.day_number);
        this.fullDay = findViewById(R.id.fullDay);
        this.startTime = findViewById(R.id.start_time);
        this.endTime = findViewById(R.id.end_time);

        // Set number picker values
        day.setMinValue(1);
        day.setMaxValue(28);
        year.setMinValue(1);
        year.setMaxValue(9999);

        // Populate month spinner with month names
        String[] months = getResources().getStringArray(R.array.months);
        month.setMinValue(1);
        month.setMaxValue(13);
        month.setDisplayedValues(months);

        // 29 days possible in june and december
        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if((newVal == 6 && CalendarDate.isLeapYear(year.getValue())) || newVal == 13) {
                    day.setMaxValue(29);
                } else {
                    day.setMaxValue(28);
                }
            }
        });
        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(month.getValue() == 6 && CalendarDate.isLeapYear(newVal)) {
                    day.setMaxValue(29);
                } else {
                    day.setMaxValue(28);
                }
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
            year.setValue(Calendar.getInstance().get(Calendar.YEAR));
            month.setValue((int)ceil(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) / (double)28));
            if((month.getValue() == 6 && CalendarDate.isLeapYear(year.getValue())) || month.getValue() == 13) {
                day.setMaxValue(29);
            } else {
                day.setMaxValue(28);
            }
            day.setValue(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)% 28);

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
                year.setValue(startDate.getYear());
                month.setValue(startDate.getMonth());
                if((month.getValue() == 6 && CalendarDate.isLeapYear(year.getValue())) || month.getValue() == 13) {
                    day.setMaxValue(29);
                } else {
                    day.setMaxValue(28);
                }
                day.setValue(startDate.getDay() + ((startDate.getWeek() - 1) * 7));

                CalendarDate endDate = event.getEndDate();
                if(endDate != null) {
                    startTime.setHour(startDate.getHour());
                    startTime.setMinute(startDate.getMinute());

                    endTime.setHour(endDate.getHour());
                    endTime.setMinute(endDate.getMinute());

                    fullDay.setChecked(false);
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
        startDate.setYear(year.getValue());
        startDate.setMonth(month.getValue());

        int selectedDay = day.getValue();
        int week = (int)(ceil((double)selectedDay/7));
        int day = selectedDay - ((week - 1) * 7);
        // Account for leap day and year day
        if(week == 5) {
            week = 4;
            day = 8;
        }

        startDate.setWeek(week);
        startDate.setDay(day);

        CalendarDate endDate = null;
        if(!fullDay.isChecked()) {
            endDate = new CalendarDate();
            endDate.setYear(year.getValue());
            endDate.setMonth(month.getValue());
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
        Resources res = getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(res.getString(R.string.delete) + " " + name.getText() + "?");
        builder.setMessage(res.getString(R.string.are_you_sure));
        builder.setPositiveButton(res.getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        long id = intent.getLongExtra("eventId", -1);
                        setResult(RESULT_OK, new Intent().putExtra("eventId", id).putExtra("wasDeleted", true));

                        CalendarRepository repository = new CalendarRepository(getApplication());
                        repository.deleteById(id);

                        finish();
                    }
                });
        builder.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private  boolean checkFields() {

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.createmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_close:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return false;
    }
}
