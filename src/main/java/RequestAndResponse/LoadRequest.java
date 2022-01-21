package RequestAndResponse;

import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;

public class LoadRequest {

    private ArrayList<User> users;
    private ArrayList<Person> persons;
    private ArrayList<Event> events;

    public ArrayList getPersons() {
        return persons;
    }

    public void setUsers(ArrayList users) {
        this.users = users;
    }

    public void setPersons(ArrayList persons) {
        this.persons = persons;
    }

    public void setEvents(ArrayList events) {
        this.events = events;
    }

    public ArrayList getEvents() {
        return events;
    }

    public ArrayList getUsers() {
        return users;
    }
}
