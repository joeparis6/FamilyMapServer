package dao;

import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private Database db;
    private User aUser;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        aUser = new User("user123", "goodpassword", "email@gmail.com", "Jeffrey",
                        "Thompson", "m","jeff123");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        uDao.insert(aUser);
        User compareUser = uDao.find(aUser.getPersonID());
        assertNotNull(compareUser);
        assertEquals(aUser, compareUser);
    }

    @Test
    public void insertFail() throws DataAccessException {
        uDao.insert(aUser);
        assertThrows(DataAccessException.class, ()-> uDao.insert(aUser));
    }

    @Test
    public void findPass() throws  DataAccessException {
        uDao.insert(aUser);
        User compareUser = uDao.find(aUser.getPersonID());
        assertNotNull(compareUser);
        assertEquals(aUser, compareUser);
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(uDao.find(aUser.getPersonID()));
    }

    @Test
    public void deletePass() throws DataAccessException {
        uDao.insert(aUser);
        User compareUser = uDao.find(aUser.getPersonID());
        assertNotNull(compareUser);
        uDao.delete();
        assertNull(uDao.find(aUser.getPersonID()));

    }
}
