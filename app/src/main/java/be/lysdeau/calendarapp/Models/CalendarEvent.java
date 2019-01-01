package be.lysdeau.calendarapp.Models;

import android.graphics.Bitmap;

// Class represents events planned by users to be displayed in the calendar
public class CalendarEvent {
    // Name of the event
    private String name;
    // Description of the event
    private String description;
    // Optional image added to the event
    private Bitmap bitmap;
    // Start and end times of the event, if endDate is null or on a different day event will be considered full day
    private CalendarDate startDate;
    private CalendarDate endDate;

    /*
        CONSTRUCTORS
     */

    public CalendarEvent() { }

    public CalendarEvent(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CalendarEvent(String name, String description, CalendarDate startDate) {
        this(name, description);
        this.startDate = startDate;
    }

    public CalendarEvent(String name, String description, CalendarDate startDate, CalendarDate endDate) {
        this(name, description, startDate);
        this.endDate = endDate;
    }

    /*
        GETTERS AND SETTERS
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CalendarDate getStartDate() {
        return startDate;
    }

    public void setStartDate(CalendarDate startDate) {
        this.startDate = startDate;
    }

    public CalendarDate getEndDate() {
        return endDate;
    }

    public void setEndDate(CalendarDate endDate) {
        this.endDate = endDate;
    }
}
