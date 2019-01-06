package be.lysdeau.calendarapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.R;

public class DateInfoFragment extends Fragment {

    public DateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_info, container, false);

        return v;
    }

    public void setDate(CalendarDate calendarDate) {

    }
}
