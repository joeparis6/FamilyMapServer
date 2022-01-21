package service;

import RequestAndResponse.LoginRequest;
import RequestAndResponse.LoginResponse;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class LoginTest {

    private LoginRequest loginRequest = new LoginRequest();
    private LoginResponse loginRepsonse = new LoginResponse();
    private Database db = new Database();
    private UserDAO uDao;
    private User Gardner;
    Login testLogin = new Login();

    @BeforeEach
    public void setUp() throws DataAccessException {

        loginRequest.setUserName("GardnerH");
        loginRequest.setPassword("solomon");
        Connection conn = db.getConnection();
        db.clearTables();
        Gardner = new User("GardnerH", "solomon", "gah@gmail.com",
                "Gardner", "Hartung", "m", "123456789");
        uDao = new UserDAO(conn);
        uDao.insert(Gardner);
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
    }


    @Test
    public void loginPass() throws DataAccessException {
        loginRepsonse = testLogin.login(loginRequest);
        assertTrue(loginRepsonse.isSuccess());

    }

    @Test
    public void loginFail() throws DataAccessException {
        loginRequest.setUserName("GardnerH");
        loginRequest.setPassword("murphy");
        loginRepsonse = testLogin.login(loginRequest);
        assertFalse(loginRepsonse.isSuccess());
        loginRequest.setUserName("JeffT");
        loginRequest.setPassword("goducks");
        loginRepsonse = testLogin.login(loginRequest);
        assertFalse(loginRepsonse.isSuccess());
    }

    @Test
    public void badParams() throws DataAccessException {
        loginRequest.setUserName("");
        loginRequest.setPassword("");
        loginRepsonse = testLogin.login(loginRequest);
        assertFalse(loginRepsonse.isSuccess());
    }
}
