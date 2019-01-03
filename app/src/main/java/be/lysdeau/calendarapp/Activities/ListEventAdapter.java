package be.lysdeau.calendarapp.Activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView eventTitle;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            eventTitle = itemView.findViewById(R.id.title_text);
        }
    }

    private List<CalendarEvent> events;

    public ListEventAdapter(List<CalendarEvent> events) {
        this.events = events;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_event_card, viewGroup, false);
        EventViewHolder holder = new EventViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventTitle.setText(events.get(i).getName());
    }


    @Override
    public int getItemCount() {
        return events.size();
    }
}
