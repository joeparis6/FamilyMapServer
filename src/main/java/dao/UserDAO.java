package dao;

import model.Person;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private final Connection conn;

    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Adds a person to the database.
     */
    public boolean insert(User user) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Users (UserName, Password, Email, FirstName, LastName, Gender, " +
                "PersonID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return true;
    }

    /**
     * Removes all users form the database.
     */
    public boolean delete() throws DataAccessException {
        String sql = "DELETE FROM Users";  //insert SQL statement
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
     * Retrieves a user form the database.
     */
    public User find(String personID) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("UserName"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                return user;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
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

    public User findUser(String userName) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("UserName"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                return user;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error encountered while finding User");
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

    public boolean deleteAllUsers(String UserName) throws DataAccessException {
        String sql = "DELETE FROM Users WHERE Username = ?";  //insert SQL statement
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
