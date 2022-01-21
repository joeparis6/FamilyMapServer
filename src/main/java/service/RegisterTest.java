package service;

import RequestAndResponse.RegisterRequest;
import RequestAndResponse.RegisterResponse;
import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class RegisterTest {

    private RegisterRequest registerRequest = new RegisterRequest();
    private RegisterResponse registerResponse = new RegisterResponse();
    private Register registerTest = new Register();
    Database db = new Database();
    UserDAO uDao;
    PersonDAO pDao;
    EventDAO eDao;
    User registeredUser;


    @BeforeEach
    public void setUp() throws DataAccessException {
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        registerRequest = new RegisterRequest();
        registerRequest.setUserName("GardnerH");
        registerRequest.setPassword("solomon");
        registerRequest.setEmail("gah@gmail.com");
        registerRequest.setFirstName("Gardner");
        registerRequest.setLastName("Hartung");
        registerRequest.setGender("m");

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
       Connection conn = db.getConnection();
       uDao = new UserDAO(conn);
       uDao.deleteAllUsers(registerRequest.getUserName());
       pDao = new PersonDAO(conn);
       pDao.deleteAllPersons(registerRequest.getUserName());
       eDao = new EventDAO(conn);
       eDao.deleteAllEvents(registerRequest.getUserName());
       db.closeConnection(true);

    }

    @Test
    public void passRegister() throws DataAccessException {

        registerResponse = registerTest.register(registerRequest);
        assertTrue(registerResponse.isSuccess());
        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        registeredUser = uDao.findUser(registerRequest.getUserName());
        assertNotNull(registeredUser);
        db.closeConnection(false);
    }

    @Test
    public void failRegister() throws DataAccessException {
        registerResponse = registerTest.register(registerRequest);
        assertTrue(registerResponse.isSuccess());
        registerResponse = registerTest.register(registerRequest);
        assertFalse(registerResponse.isSuccess());
    }



}