package dao;

import model.Event;
import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Adds a person to the database.
     */
    public boolean insert(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return true;
    }

    /**
     * Removes all persons from the database.
     */
    public boolean delete() throws DataAccessException {
        String sql = "DELETE FROM Persons";  //insert SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        return true;
    }

    /**
     * Retrieves a person from the database.
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public ArrayList<Person> getAllPersons(String associatedUsername) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<Person>();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("AssociatedUsername"),
                                    rs.getString("FirstName"), rs.getString("LastName"),
                                    rs.getString("Gender"), rs.getString("FatherID"),
                                    rs.getString("MotherID"), rs.getString("SpouseID"));
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return persons;
    }

    public boolean deleteAllPersons(String associatedUserName) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?";  //insert SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return true;
    }
}
