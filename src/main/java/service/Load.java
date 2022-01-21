package service;

import RequestAndResponse.LoadRequest;
import RequestAndResponse.LoadResponse;
import dao.*;
import model.Event;
import model.Person;
import model.User;

import java.sql.Connection;
import java.util.ArrayList;

public class Load {

    LoadResponse confirmLoad = new LoadResponse();
    Database db = new Database();

    /**
     * Clears all data from the database (just like the /clear API),
     * and then loads the posted user, person, and event data into the database.
     * @param r the incoming request
     * @return a response containing a success or failure message
     */
    public LoadResponse load(LoadRequest r) throws DataAccessException {
        System.out.println("loading");
        Connection conn = db.getConnection();
        UserDAO uDao = new UserDAO(conn);
        PersonDAO pDao = new PersonDAO(conn);
        EventDAO eDao = new EventDAO(conn);

        db.clearTables();
        System.out.println("tables cleared");
        ArrayList requestUsers = r.getUsers();
        System.out.println(requestUsers);
        ArrayList requestPersons = r.getPersons();
        ArrayList requestEvents = r.getEvents();

        for (Object insertUser : requestUsers) {
            try {
                uDao.insert((User) insertUser);
            } catch (DataAccessException e) {
                e.printStackTrace();
                confirmLoad = generateFailureMessage("Insertion of Users unsuccessful");
                return confirmLoad;
            }

        }

        for (Object insertPerson : requestPersons) {
            try {
                pDao.insert((Person) insertPerson);
            } catch (DataAccessException e) {
                e.printStackTrace();
                confirmLoad = generateFailureMessage("Insertion of Persons unsuccessful");
                return confirmLoad;
            }
        }

        for (Object insertEvent : requestEvents) {
            try {
                eDao.insert((Event) insertEvent);
            } catch (DataAccessException e) {
                e.printStackTrace();
                confirmLoad = generateFailureMessage("Insertion of Events unsuccessful");
                return confirmLoad;
            }
        }

        db.closeConnection(true);

        confirmLoad.setMessage("Successfully added "
                                + requestUsers.size() + " users, "
                                + requestPersons.size() + " persons, and "
                                + requestEvents.size() + " events to the database.");
        confirmLoad.setSuccess(true);
        System.out.println("response sent");
        return confirmLoad;
    }

    public LoadResponse generateFailureMessage(String message) throws DataAccessException {
        confirmLoad.setSuccess(false);
        confirmLoad.setMessage(message);
        db.closeConnection(false);
        return confirmLoad;
    }

}
