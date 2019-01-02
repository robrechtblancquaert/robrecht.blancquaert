package be.lysdeau.calendarapp.Database;

import java.util.List;

public interface DataCallback<T> {
    void dataReceived(List<T> data);
}
