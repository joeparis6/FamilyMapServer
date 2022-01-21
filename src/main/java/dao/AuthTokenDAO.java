package dao;

import model.AuthToken;
import model.Person;

import java.sql.*;
import java.sql.SQLException;

public class AuthTokenDAO {

    private final Connection conn;

    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Adds a new AuthToken to the database.
     */
    public boolean insert(AuthToken authToken) throws DataAccessException {

        String sql = "INSERT INTO AuthTokens (UserAuthToken, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, authToken.getUserAuthToken());
            stmt.setString(2, authToken.getUserName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return true;
    }

    /**
     * Removes all AuthTokens from the database.
     */
    public boolean delete() throws DataAccessException {
        String sql = "DELETE FROM AuthTokens";  //insert SQL statement
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
     * Retrieves an AuthToken associated with a particular username from the database
     */
    public AuthToken find(String userName) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE userName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("UserAuthToken"), rs.getString("Username"));
                return authToken;
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

    public AuthToken findAuthToken(String authToken) throws DataAccessException {
        AuthToken foundToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE userAuthToken = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                foundToken = new AuthToken(rs.getString("UserAuthToken"), rs.getString("Username"));
                return foundToken;
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

    public AuthToken findUsernameFromAuthToken(String authTokenString) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE UserAuthToken = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authTokenString);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("UserAuthToken"), rs.getString("Username"));
                return authToken;
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

}
