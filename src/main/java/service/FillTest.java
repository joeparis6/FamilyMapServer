package service;

import RequestAndResponse.FillRequest;
import RequestAndResponse.FillResponse;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import dao.UserDAO;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.Connection;

public class FillTest {

    private Fill testFill = new Fill();
    private FillRequest fillRequest;
    private FillResponse fillResponse = new FillResponse();
    Database db = new Database();
    User Gardner;
    UserDAO uDao;
    PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        fillRequest = new FillRequest("GardnerH", "6");
        Gardner = new User("GardnerH", "solomon", "gah@gmail.com",
                "Gardner", "Hartung", "m", "123456789");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDAO(conn);
        uDao.insert(Gardner);
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void passFill() throws IOException, DataAccessException {
        fillResponse = testFill.fill(fillRequest);
        assertTrue(fillResponse.isSuccess());
        assertEquals(fillResponse.getMessage(),
                "Successfully added 127 persons and 379 events to the database.");

    }

    @Test
    public void failFill() throws IOException, DataAccessException {
        fillRequest = new FillRequest("GardnerH", "-6");
        fillResponse = testFill.fill(fillRequest);
        assertFalse(fillResponse.isSuccess());
    }

}
