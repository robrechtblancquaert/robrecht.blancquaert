package be.lysdeau.calendarapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.lysdeau.calendarapp.R;

public class EventCardFragment extends Fragment {

    private EventCardListener mCallback;

    public EventCardFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_card, container, false);

        // Add listeners to interactions and do callback on mCallback

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventCardListener) {
            mCallback = (EventCardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EventCardListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface EventCardListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
