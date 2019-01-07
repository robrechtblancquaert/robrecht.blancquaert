package be.lysdeau.calendarapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.lysdeau.calendarapp.Activities.ListEventActivity;
import be.lysdeau.calendarapp.Database.CalendarRepository;
import be.lysdeau.calendarapp.Database.DataCallback;
import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

public class DateInfoFragment extends Fragment {

    TextView fixedText;
    TextView gregText;
    TextView eventCount;
    String[] months;

    ListView listView;
    List<String> data;
    List<Long> ids;
    ArrayAdapter<String> adapter;

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

        this.listView = v.findViewById(R.id.list_view_info);
        this.data = new ArrayList<String>();
        this.ids = new ArrayList<Long>();

        adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        months = getResources().getStringArray(R.array.months);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().startActivityForResult(new Intent(getActivity(), ListEventActivity.class).putExtra("jumpToId", ids.get(position)), 0);
            }
        });

        return v;
    }

    public void setDate(CalendarDate calendarDate) {
        String fixedString = String.format("the %s of %s, %s", toOrdinal(calendarDate.getDay() +((calendarDate.getWeek() - 1) * 7)),
                months[calendarDate.getMonth() - 1],
                ((Integer)calendarDate.getYear()).toString());
        fixedText.setText(fixedString);
        if(calendarDate.isLeapDay()) fixedText.setText(getResources().getString(R.string.leap_day));
        if(calendarDate.isYearDay()) fixedText.setText(getResources().getString(R.string.year_day));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendarDate.getYear());
        calendar.set(Calendar.DAY_OF_YEAR, calendarDate.getDay() + ((calendarDate.getWeek() - 1) * 7) + ((calendarDate.getMonth() - 1) * 28));

        int month = calendar.get(Calendar.MONTH);
        if(month > 6) month++;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String gregString = String.format("the %s of %s, %s.",
                toOrdinal(day), months[month],
                String.valueOf(year));
        gregText.setText(gregString);

        doList(calendarDate);
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

    private void doList(CalendarDate calendarDate) {
        CalendarRepository repository = new CalendarRepository(getActivity().getApplication());
        repository.getEventsByDate(calendarDate, new DataCallback<CalendarEvent>() {
            @Override
            public void dataReceived(List<CalendarEvent> dataR) {
                data.clear();
                ids.clear();
                for(CalendarEvent event : dataR) {
                    data.add(event.getName());
                    ids.add(event.getId());
                }
                updateListData();
            }
        });
    }

    private void updateListData() {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                eventCount.setText(new Integer(data.size()).toString());
            }
        });
    }
}
