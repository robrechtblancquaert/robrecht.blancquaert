package be.lysdeau.calendarapp;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import be.lysdeau.calendarapp.Models.CalendarDate;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CalendarDateTest {

    CalendarDate date;

    @Before
    public void before() {
        this.date = new CalendarDate();
    }

    @Test
    public void setDay_is_correct() {
        date.setDay(1);
        assertEquals(1, date.getDay());

        date.setDay(7);
        assertEquals(7, date.getDay());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDay_exception() {
        date.setDay(8);
    }

    @Test
    public void setDay_no_exception_leap_and_year_day() {

        date.setYear(2020);
        date.setMonth(6);
        date.setWeek(4);
        date.setDay(8);
        assertEquals(8, date.getDay());

        date.setMonth(13);
        date.setDay(8);
        assertEquals(8, date.getDay());
    }

    @Test
    public void setWeek_is_correct() {
        date.setWeek(1);
        assertEquals(1, date.getWeek());

        date.setWeek(4);
        assertEquals(4, date.getWeek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWeek_exception() {
        date.setWeek(0);
    }

    @Test
    public void setMonth_is_correct() {
        date.setMonth(1);
        assertEquals(1, date.getMonth());

        date.setMonth(13);
        assertEquals(13, date.getMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMonth_exception() {
        date.setMonth(-5);
    }

    @Test
    public void setYear_is_correct() {
        date.setYear(1);
        assertEquals(1, date.getYear());

        date.setYear(9999);
        assertEquals(9999, date.getYear());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setYear_exception() {
        date.setYear(0);
    }

    @Test
    public void isLeapYear_is_correct() {
        assertTrue(date.isLeapYear(2020));
        assertTrue(date.isLeapYear(2000));

        assertFalse(date.isLeapYear(2019));
        assertFalse(date.isLeapYear(1900));
    }

    @Test
    public void isLeapDay_is_correct() {
        date.setYear(2020);
        date.setMonth(6);
        date.setWeek(4);
        date.setDay(8);

        assertTrue(date.isLeapDay());
    }

    @Test
    public void isYearDay_is_correct() {
        date.setYear(2020);
        date.setMonth(13);
        date.setWeek(4);
        date.setDay(8);

        assertTrue(date.isYearDay());
    }
}