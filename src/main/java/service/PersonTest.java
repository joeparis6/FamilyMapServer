package service;

import RequestAndResponse.PersonResponse;
import RequestAndResponse.RegisterRequest;
import RequestAndResponse.RegisterResponse;
import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.sql.Connection;

public class PersonTest {

    private PersonResponse personResponse = new PersonResponse();
    private PersonService testPersonService = new PersonService();
    private Database db = new Database();
    PersonDAO pDao;
    User Gardner;
    Person Jeffrey;
    String authToken;
    String fakeAuthToken;

    @BeforeEach
    public void setUp() throws DataAccessException {
        Gardner = new User("GardnerH", "solomon", "gah@gmail.com",
                           "Gardner", "Hartung", "m", "123456789");
        Jeffrey = new Person("gardnerfriend", "GardnerH", "Jeffrey",
                             "Thompson", "m", "jeffdad", "jeffmom", "jeffwife");
        Connection conn = db.getConnection();
        db.clearTables();
        pDao = new PersonDAO(conn);
        pDao.insert(Jeffrey);
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
    public void passPersonService() throws DataAccessException {
        personResponse = testPersonService.personService(null, authToken, true);
        assertTrue(personResponse.isSuccess());
        assertTrue(personResponse.getPersons().contains(Jeffrey));
        personResponse = testPersonService.personService("gardnerfriend", authToken, false);
        assertTrue(personResponse.isSuccess());
        assertEquals(personResponse.getFirstName(), "Jeffrey");
    }

    @Test
    public void failPersonService() throws DataAccessException {
        personResponse = testPersonService.personService(null, fakeAuthToken, true);
        assertFalse(personResponse.isSuccess());
        assertNull(personResponse.getPersons());
        personResponse = testPersonService.personService("gardnerfriend", fakeAuthToken, false);
        assertFalse(personResponse.isSuccess());
        assertNull(personResponse.getFirstName());
    }
}
