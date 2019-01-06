package be.lysdeau.calendarapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.R;

import static java.lang.Math.ceil;

public class DateJumpFragment extends Fragment {

    private OnDateChangedListener mListener;

    NumberPicker dayPicker;
    NumberPicker monthPicker;
    NumberPicker yearPicker;

    public DateJumpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_jump, container, false);

        dayPicker = v.findViewById(R.id.day_picker);
        monthPicker = v.findViewById(R.id.month_picker);
        yearPicker = v.findViewById(R.id.year_picker);

        NumberPicker.OnValueChangeListener valueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                triggerChange();
            }
        };

        dayPicker.setOnValueChangedListener(valueChangeListener);
        monthPicker.setOnValueChangedListener(valueChangeListener);
        yearPicker.setOnValueChangedListener(valueChangeListener);

        yearPicker.setMinValue(1);
        yearPicker.setMaxValue(9999);

        dayPicker.setMinValue(1);
        // max is set in triggerChange()

        String[] months = getResources().getStringArray(R.array.months);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(13);
        monthPicker.setDisplayedValues(months);

        yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));
        monthPicker.setValue((int)ceil(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) / (double)28));
        triggerChange();
        dayPicker.setValue(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)% 28);

        Button button = v.findViewById(R.id.button_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDate calendarDate = new CalendarDate();

                int month = monthPicker.getValue();
                int year = yearPicker.getValue();

                calendarDate.setYear(year);
                calendarDate.setMonth(month);

                int selectedDay = dayPicker.getValue();
                int week = (int)(ceil((double)selectedDay/7));
                int day = selectedDay - ((week - 1) * 7);
                // Account for leap day and year day
                if(week == 5) {
                    week = 4;
                    day = 8;
                }

                calendarDate.setWeek(week);
                calendarDate.setDay(day);

                mListener.onDateChanged(calendarDate);
            }
        });

        button.callOnClick();

        return v;
    }

    private void triggerChange() {
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();

        if((month == 6 && CalendarDate.isLeapYear(year)) || month == 13) {
            dayPicker.setMaxValue(29);
        } else {
            dayPicker.setMaxValue(28);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDateChangedListener) {
            mListener = (OnDateChangedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDateChangedListener {
        void onDateChanged(CalendarDate calendarDate);
    }
}
