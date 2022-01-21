package dao;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {

    private Database db;
    private Person aPerson;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        aPerson = new Person("joe123", "joeP", "Joe", "Paris",
                "m", "dave123", "amy123", "myspouse123");
        Connection conn = db.getConnection();
        db.clearTables();
        pDao = new PersonDAO(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insert(aPerson);
        Person comparePerson = pDao.find(aPerson.getPersonID());
        assertNotNull(comparePerson);
        assertEquals(aPerson, comparePerson);
    }

    @Test
    public void insertFail() throws DataAccessException {
        pDao.insert(aPerson);
        assertThrows(DataAccessException.class, ()-> pDao.insert(aPerson));
    }

    @Test
    public void findPass() throws  DataAccessException {
        pDao.insert(aPerson);
        Person comparePerson = pDao.find(aPerson.getPersonID());
        assertNotNull(comparePerson);
        assertEquals(aPerson, comparePerson);
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(pDao.find(aPerson.getPersonID()));
    }

    @Test
    public void deletePass() throws DataAccessException {
        pDao.insert(aPerson);
        Person comparePerson = pDao.find(aPerson.getPersonID());
        assertNotNull(comparePerson);
        pDao.delete();
        assertNull(pDao.find(aPerson.getPersonID()));
    }
}
