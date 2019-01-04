package be.lysdeau.calendarapp.Models;

// Custom date object because we use our own calendar
public class CalendarDate {
    // Integer between 0 and 9999
    private int year;
    // Month must be between 1 and 13, the 7th month is Sol
    private int month;
    // Week must be between 1 and 4
    private int week;
    // Day between 1 and 8, first day of the week is Sunday and the 8th day can bu used to represent the year or leap days
    private int day;
    // Integer between 0 and 23
    private int hour;
    // Integer between 0 and 59
    private int minute;

    /*
        CONSTRUCTORS
     */

    public CalendarDate() { }

    public CalendarDate(int year, int month, int week, int day) {
        setYear(year);
        setMonth(month);
        setWeek(week);
        setDay(day);
    }

    public CalendarDate(int year, int month, int week, int day, int hour) {
        this(year, month, week, day);
        setHour(hour);
    }

    public CalendarDate(int year, int month, int week, int day, int hour, int minute) {
        this(year, month, week, day, hour);
        setMinute(minute);
    }

    /*
        GETTERS AND SETTERS
     */

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if(year < 0 || year > 9999) throw new IllegalArgumentException("Year must be between 0 and 9999");
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if(month < 1 || month > 13) throw new IllegalArgumentException("Month must be between 1 and 13");
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        if(week < 1 || week > 4) throw new IllegalArgumentException("Week must be between 1 and 4");
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if(day < 1 || day > 8) throw new IllegalArgumentException("Day must be between 1 and 8");
        if(day == 8 && !(week == 4 && (month == 7 || month == 13))) throw new IllegalArgumentException("8th day may only occur in Sol and December");
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if(hour < 0 || hour > 23) throw new IllegalArgumentException("Hour must be between 0 and 23");
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if(minute < 0 || minute > 59) throw new IllegalArgumentException("Minute must be between 0 and 59");
        this.minute = minute;
    }

    /*
        FUNCTIONS
     */

    public boolean isLeapYear() {
        if(year % 4 == 0) {
            if(year % 100 == 0) {
                return year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public  boolean isLeapDay() {
        return (month == 6 && week == 4 && day == 8);
    }

    public boolean isYearDay() {
        return (month == 13 && week == 4 && day == 8);
    }
}
