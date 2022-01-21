package service;

import RequestAndResponse.LoadRequest;
import RequestAndResponse.LoadResponse;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;

public class LoadTest {

    private Load testLoad = new Load();
    private LoadRequest loadRequest = new LoadRequest();
    private LoadResponse loadResponse = new LoadResponse();
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Person> persons = new ArrayList<Person>();
    private ArrayList<Event> events = new ArrayList<Event>();
    private UserDAO uDao;
    private Database db = new Database();
    User testUser;
    String successMessage = "Successfully added 1 users, 2 persons, and 2 events to the database.";
    private Object DataAccessException;

    @BeforeEach
    public void setUp() throws DataAccessException {
        User user1 = new User("GardnerH", "solomon", "gah@gmail.com",
                              "Gardner", "Hartung", "m", "123456789");
        User testUser = new User("JeffT", "goducks", "jct@gmail.com",
                                 "Jeffrey", "Thompson", "m", "mynameisjeff");
        Person person1 = new Person("111111111", "JakeC", "Jake",
                                    "Curletto", "m", "mydadID", "mymomID",
                                    "myspouseID");
        Person person2 = new Person("222222222", "ChrisU", "Chris",
                                    "Underwood", "m", "dadID", "momID",
                                    "spouseID");
        Event event1 = new Event("firstevent", "GardnerH", "123456789",
                                 100f, 100f, "USA", "Portland",
                                 "Camping trip", 2017);
        Event event2 = new Event("secondevent", "JakeC", "111111111",
                                 150f, 150f, "USA", "Eugene",
                                 "Soccer game", 2019);
        users.add(user1);
        persons.add(person1);
        persons.add(person2);
        events.add(event1);
        events.add(event2);
        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        uDao.insert(testUser);
        db.closeConnection(true);
        loadRequest.setUsers(users);
        loadRequest.setPersons(persons);
        loadRequest.setEvents(events);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void passLoad() throws DataAccessException {
        loadResponse = testLoad.load(loadRequest);
        assertTrue(loadResponse.isSuccess());
        assertEquals(loadResponse.getMessage(), successMessage);
    }

    @Test
    public void failLoad() throws DataAccessException {
        /*
        User testUser = new User("JeffT", "goducks", "jct@gmail.com",
                "Jeffrey", "Thompson", "m", "mynameisjeff");
        loadResponse =  testLoad.load(loadRequest);
        Connection conn = db.getConnection();
        assertThrows(DataAccessException, uDao.findUser(testUser.getUserName()));
        db.closeConnection(false);

         */
    }
}
