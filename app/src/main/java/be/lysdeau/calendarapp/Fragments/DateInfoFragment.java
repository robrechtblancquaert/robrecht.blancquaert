package be.lysdeau.calendarapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.R;

public class DateInfoFragment extends Fragment {

    TextView fixedText;
    TextView gregText;
    TextView eventCount;
    String[] months;

    public DateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_info, container, false);

        this.fixedText = v.findViewById(R.id.fixed_date_text);
        this.gregText = v.findViewById(R.id.greg_date_text);
        this.eventCount = v.findViewById(R.id.event_count);

        months = getResources().getStringArray(R.array.months);

        return v;
    }

    public void setDate(CalendarDate calendarDate) {
        String fixedString = String.format("the %s of %s, %s", toOrdinal(calendarDate.getDay()), months[calendarDate.getMonth() - 1], ((Integer)calendarDate.getYear()).toString());
        fixedText.setText(fixedString);

        Calendar calendar = Calendar.getInstance();

    }

    public String toOrdinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }
}
