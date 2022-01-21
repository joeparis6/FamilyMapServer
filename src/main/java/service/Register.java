package service;


import RequestAndResponse.FillRequest;
import RequestAndResponse.RegisterRequest;
import RequestAndResponse.RegisterResponse;
import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
import model.User;

import java.io.IOException;
import java.sql.Connection;
import java.util.UUID;

public class Register {

    private RegisterResponse confirmRegister = new RegisterResponse();
    private Database db = new Database();
    User searchUser;
    String authToken;
    String personID;
    String userName;


    public RegisterResponse register(RegisterRequest r) throws DataAccessException {
        Connection conn = db.getConnection();
        UserDAO uDao = new UserDAO(conn);
        AuthTokenDAO aDao = new AuthTokenDAO(conn);
        try {
            searchUser = uDao.findUser(r.getUserName());
            if (searchUser != null) {
                return generateFailureResponse("User not found error");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return generateFailureResponse("User not found error");
        }
        User registerUser = new User(r.getUserName(), r.getPassword(), r.getEmail(), r.getFirstName(),
                                     r.getLastName(), r.getGender(), generateUniqueID());
        try {
            uDao.insert(registerUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return generateFailureResponse("User insertion error");
        }

        authToken = generateUniqueID();
        AuthToken registerAuthToken =new AuthToken(authToken, registerUser.getUserName());
        try {
            aDao.insert(registerAuthToken);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            return generateFailureResponse("AuthToken Insertion error");
        }

        personID = registerUser.getPersonID();
        userName = r.getUserName();
        confirmRegister = generateSuccessResponse();
        fillGenerations();
        return confirmRegister;
    }

    public String generateUniqueID() {
        String uniqueID;
        UUID uuid = UUID.randomUUID();
        uniqueID = uuid.toString();
        return uniqueID;
    }

    public RegisterResponse generateFailureResponse(String message) throws DataAccessException {
        confirmRegister.setSuccess(false);
        confirmRegister.setMessage(message);
        confirmRegister.setAuthToken(null);
        confirmRegister.setPersonID(null);
        confirmRegister.setUserName(null);
        db.closeConnection(false);
        return confirmRegister;
    }

    public RegisterResponse generateSuccessResponse() throws DataAccessException {
        confirmRegister.setPersonID(personID);
        confirmRegister.setAuthToken(authToken);
        confirmRegister.setUserName(userName);
        confirmRegister.setMessage("Register Successful");
        confirmRegister.setSuccess(true);
        db.closeConnection(true);
        return confirmRegister;
    }

    public void fillGenerations() throws DataAccessException {
        FillRequest fourGenerations = new FillRequest(userName, "4");
        Fill fillService = new Fill();
        try {
            fillService.fill(fourGenerations);
        } catch (DataAccessException | IOException e) {
            e.printStackTrace();
            confirmRegister = generateFailureResponse("Register error: could not add family data");
            return;
        }


    }

}


