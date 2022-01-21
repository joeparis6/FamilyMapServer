package service;

import RequestAndResponse.*;
import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private EventResponse eventResponse = new EventResponse();
    private AllEventsResponse allEventsResponse = new AllEventsResponse();
    private EventService testEventService = new EventService();
    private Database db = new Database();
    private EventDAO eDao;
    private User Gardner;
    private Event campingTrip;
    private String authToken;
    private String fakeAuthToken;

    @BeforeEach
    public void setUp() throws DataAccessException {
        Gardner = new User("GardnerH", "solomon", "gah@gmail.com",
                "Gardner", "Hartung", "m", "123456789");
        campingTrip = new Event("firstevent", "GardnerH", "123456789",
                                 100f, 100f, "USA", "Portland",
                                 "Camping trip", 2017);
        Connection conn = db.getConnection();
        db.clearTables();
        eDao = new EventDAO(conn);
        eDao.insert(campingTrip);
        db.closeConnection(true);

        Register testRegister = new Register();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(Gardner.getUserName());
        registerRequest.setPassword(Gardner.getPassword());
        registerRequest.setEmail(Gardner.getEmail());
        registerRequest.setFirstName(Gardner.getFirstName());
        registerRequest.setLastName(Gardner.getLastName());
        registerRequest.setGender(Gardner.getGender());

        RegisterResponse registerResponse = testRegister.register(registerRequest);
        authToken = registerResponse.getAuthToken();
        fakeAuthToken = "hb65t6y89kl12";

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void passEventService() throws DataAccessException {
        allEventsResponse = testEventService.getAllEvents(authToken);
        assertTrue(allEventsResponse.isSuccess());
        assertTrue(allEventsResponse.getEvents().contains(campingTrip));
        eventResponse = testEventService.getEvent("firstevent" , authToken);
        assertTrue(eventResponse.isSuccess());
    }

    @Test
    public void failEventService() throws DataAccessException {
        allEventsResponse = testEventService.getAllEvents(fakeAuthToken);
        assertFalse(allEventsResponse.isSuccess());
        assertNull(allEventsResponse.getEvents());
        eventResponse = testEventService.getEvent("firstevent" , fakeAuthToken);
        assertFalse(eventResponse.isSuccess());
    }


}
