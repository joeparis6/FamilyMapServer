package service;

import RequestAndResponse.LoginRequest;
import RequestAndResponse.LoginResponse;
import dao.*;
import model.AuthToken;
import model.User;

import java.sql.Connection;
import java.util.UUID;

public class Login {

    LoginResponse loginResponse = new LoginResponse();
    String username;
    String password;
    String personID;
    String authToken;
    Database db = new Database();
    UserDAO uDao;
    AuthTokenDAO aDao;
    User compareUser;


    /**
     * Logs in the user and returns an auth token.
     * @param r
     * @return a response containing the auth token, username, and personID
     */
    public LoginResponse login(LoginRequest r) throws DataAccessException {
        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        aDao = new AuthTokenDAO(conn);


        username = r.getUserName();
        password = r.getPassword();
        if (!verifyLoginInfo()) {
            loginResponse = generateFailureResponse("Login error: invalid user info");
            db.closeConnection(false);
            return loginResponse;
        }

        try {
           User searchUser = uDao.findUser(username);
           compareUser = searchUser;
        }
        catch (DataAccessException e) {
            loginResponse = generateFailureResponse("Login error: data access error");
            db.closeConnection(false);
            return loginResponse;
        }

        if (compareUser != null && compareUser.getPassword().equals(password)) {
            personID = compareUser.getPersonID();
        }
        else {
            loginResponse = generateFailureResponse("Login error: incorrect password");
            db.closeConnection(false);
            return loginResponse;
        }

        authToken = generateUniqueID();
        AuthToken registerAuthToken =new AuthToken(authToken, username);
        try {
            aDao.insert(registerAuthToken);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            return generateFailureResponse("Login error");
        }

        loginResponse = generateSuccessResponse();
        db.closeConnection(true);
        return loginResponse;
    }

    public String generateUniqueID() {
        String uniqueID;
        UUID uuid = UUID.randomUUID();
        uniqueID = uuid.toString(); //UUID.randomUUID().toString().replace("-", "");
        return uniqueID;
    }

    public LoginResponse generateFailureResponse(String failureMessage) {
        loginResponse.setMessage(failureMessage);
        loginResponse.setSuccess(false);
        loginResponse.setAuthToken(null);
        loginResponse.setPersonID(null);
        loginResponse.setUserName(null);
        return loginResponse;
    }

    public LoginResponse generateSuccessResponse() {
        loginResponse.setMessage("Login successful");
        loginResponse.setSuccess(true);
        loginResponse.setAuthToken(authToken);
        loginResponse.setPersonID(personID);
        loginResponse.setUserName(username);
        return loginResponse;
    }

    public boolean verifyLoginInfo() {
        if (username.equals("") || password.equals("")) {
            return false;
        }
        return true;
    }
}
