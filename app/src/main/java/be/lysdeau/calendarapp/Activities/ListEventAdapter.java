package be.lysdeau.calendarapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.List;

import be.lysdeau.calendarapp.Models.CalendarDate;
import be.lysdeau.calendarapp.Models.CalendarEvent;
import be.lysdeau.calendarapp.R;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.EventViewHolder> {

    static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView eventTitle;
        TextView description;

        TextView date;

        TextView startText;
        TextView startTime;

        TextView endText;
        TextView endTime;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            eventTitle = itemView.findViewById(R.id.title_text);
            description = itemView.findViewById(R.id.description_text);

            date = itemView.findViewById(R.id.card_event_date);

            startText = itemView.findViewById(R.id.card_event_start_text);
            startTime = itemView.findViewById(R.id.card_event_start);

            endText = itemView.findViewById(R.id.card_event_end_text);
            endTime = itemView.findViewById(R.id.card_event_end);
        }
    }

    private List<CalendarEvent> events;
    private Context context;

    public ListEventAdapter(List<CalendarEvent> events, Context context) {
        this.events = events;
        this.context = context;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_event_card, viewGroup, false);
        EventViewHolder holder = new EventViewHolder(itemView);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Snackbar snackbar = Snackbar.make(itemView, R.string.event_saved, Snackbar.LENGTH_LONG);
                //snackbar.show();
                Intent intent = new Intent(context, CreateEventActivity.class);
                intent.putExtra("eventId", i);
                context.startActivity(intent);
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventTitle.setText(events.get(i).getName());
        eventViewHolder.description.setText(events.get(i).getDescription());

        CalendarDate startDate = events.get(i).getStartDate();
        CalendarDate endDate = events.get(i).getEndDate();
        String startDateString = String.format("%02d %s %d",
                (startDate.getDay() + (7 * startDate.getWeek())),
                context.getResources().getStringArray(R.array.months)[(startDate.getMonth() - 1)],
                startDate.getYear());
        if(startDate.isLeapDay()) startDateString = context.getResources().getString(R.string.leap_day);
        if(startDate.isYearDay()) startDateString = context.getResources().getString(R.string.year_day);
        eventViewHolder.date .setText(startDateString);

        if(endDate != null) {
            eventViewHolder.startTime.setText(String.format("%02d:%02d", startDate.getHour(), startDate.getMinute()));
            eventViewHolder.endTime.setText(String.format("%02d:%02d", endDate.getHour(), endDate.getMinute()));
        } else {
            eventViewHolder.startText.setVisibility(View.INVISIBLE);
            eventViewHolder.endText.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return events.size();
    }
}
