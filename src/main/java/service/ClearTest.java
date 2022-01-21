package service;

import RequestAndResponse.ClearResponse;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.sql.Connection;

public class ClearTest {

    Clear testClear = new Clear();
    ClearResponse clearResponse = new ClearResponse();
    Database db = new Database();
    UserDAO uDao;
    User Gardner;

    @BeforeEach
    public void setUp() throws DataAccessException {
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDAO(conn);
        Gardner = new User("GardnerH", "solomon", "gah@gmail.com",
                "Gardner", "Hartung", "m", "123456789");
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
    public void clearFullDatabase() throws DataAccessException {
        clearResponse = testClear.clear();
        assertTrue(clearResponse.isSuccess());
    }

    @Test
    public void fail() throws DataAccessException {
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        clearResponse = testClear.clear();
        assertTrue(clearResponse.isSuccess());
    }
}
