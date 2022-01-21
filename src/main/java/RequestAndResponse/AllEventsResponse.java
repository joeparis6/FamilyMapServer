package RequestAndResponse;

import model.Event;

import java.util.ArrayList;

public class AllEventsResponse extends ServiceResponse {

    private ArrayList data;

    public ArrayList getEvents() {
        return data;
    }

    public void setEvents(ArrayList data) {
        this.data = data;
    }


}
