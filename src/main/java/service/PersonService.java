package service;

import RequestAndResponse.PersonResponse;
import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.AuthToken;
import model.Person;

import java.sql.Connection;
import java.util.ArrayList;

public class PersonService {

    private Database db = new Database();
    private AuthTokenDAO aDao;
    private PersonDAO pDao;
    private AuthToken authTokenObject;
    private Person searchedPerson;
    String userName;
    PersonResponse confirmPersonService = new PersonResponse();
    ArrayList<Person> data = null;

    /**
     * Returns the single Person object with the specified ID
     * @param personID
     * @param authToken
     * @return a response containing the info of the person object
     */
    public PersonResponse personService(String personID, String authToken, boolean allPersons) throws DataAccessException {
        Connection conn = db.getConnection();
        aDao = new AuthTokenDAO(conn);
        pDao = new PersonDAO(conn);

        try {
            authTokenObject = aDao.findUsernameFromAuthToken(authToken);
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmPersonService = generateFailureResponse("Invalid Authtoken error");
            db.closeConnection(false);
            return confirmPersonService;
        }

        if (!(authTokenObject == null)) {
            userName = authTokenObject.getUserName();
        }
        else {
            confirmPersonService = generateFailureResponse("Invalid Authtoken error");
            db.closeConnection(false);
            return confirmPersonService;
        }

        if (allPersons) {
            confirmPersonService = getFamily();
        }
        else {
            confirmPersonService = getPerson(personID);
        }

        return confirmPersonService;
    }

    /**
     * Returns ALL family members of the current user.
     * The current user is determined from the provided auth token.
     * @return a response that contains an array of person objects
     */
    public PersonResponse getFamily() throws DataAccessException {

        try {
           data = pDao.getAllPersons(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmPersonService = generateFailureResponse("Extended family search error");
            db.closeConnection(false);
            return confirmPersonService;
        }

        confirmPersonService = generateFailureResponse(null);
        confirmPersonService.setSuccess(true);
        confirmPersonService.setPersons(data);
        db.closeConnection(true);
        return confirmPersonService;
    }

    public PersonResponse getPerson(String personID) throws DataAccessException {

        try {
            searchedPerson = pDao.find(personID);
            if (searchedPerson == null) {
                confirmPersonService = generateFailureResponse("Person not found error");
                db.closeConnection(false);
                return confirmPersonService;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmPersonService = generateFailureResponse("Person not found error");
            db.closeConnection(false);
            return confirmPersonService;
        }

        if (!searchedPerson.getAssociatedUsername().equals(userName)) {
            confirmPersonService = generateFailureResponse("Invalid person error");
            db.closeConnection(false);
            return confirmPersonService;
        }

        confirmPersonService = generateSuccessResponse("Person successfully obtained");
        db.closeConnection(true);
        return confirmPersonService;
    }

    public PersonResponse generateFailureResponse(String message) throws DataAccessException {
        confirmPersonService.setMessage(message);
        confirmPersonService.setSuccess(false);
        confirmPersonService.setAssociatedUsername(null);
        confirmPersonService.setFatherID(null);
        confirmPersonService.setFirstName(null);
        confirmPersonService.setLastName(null);
        confirmPersonService.setFatherID(null);
        confirmPersonService.setMotherID(null);
        confirmPersonService.setSpouseID(null);
        confirmPersonService.setGender(null);
        confirmPersonService.setPersons(null);
        return confirmPersonService;
    }

    public PersonResponse generateSuccessResponse(String message) throws DataAccessException {
        confirmPersonService.setMessage(message);
        confirmPersonService.setSuccess(true);
        confirmPersonService.setPersonID(searchedPerson.getPersonID());
        confirmPersonService.setAssociatedUsername(searchedPerson.getAssociatedUsername());
        confirmPersonService.setFirstName(searchedPerson.getFirstName());
        confirmPersonService.setLastName(searchedPerson.getLastName());
        confirmPersonService.setFatherID(searchedPerson.getFatherID());
        confirmPersonService.setMotherID(searchedPerson.getMotherID());
        confirmPersonService.setSpouseID(searchedPerson.getSpouseID());
        confirmPersonService.setGender(searchedPerson.getGender());
        confirmPersonService.setPersons(null);

        return confirmPersonService;
    }
}
